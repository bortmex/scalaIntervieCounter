package controllers

import entity.{Global, User}
import formz.{UserForm, UserFormData}
import play.api.data.Form
import play.api.libs.json._
import play.api.mvc._
import services.UserService

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserController @Inject()(
                                cc: MessagesControllerComponents,
                                userService: UserService
                              ) extends MessagesAbstractController(cc) {

  implicit val userFormat = Json.format[User]

  def getAll = Action.async { implicit request: Request[AnyContent] =>
    userService.listAllItems map { items =>
      Ok(Json.toJson(items))
    }
  }

  def getById(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    userService.getItem(id) map { item =>
      Ok(Json.toJson(item))
    }
  }

  def add = Action.async { implicit request: Request[AnyContent] =>
    UserForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => {
        errorForm.errors.foreach(println)
        Future.successful(BadRequest("Error!"))
      },
      data => {
        val newTodoItem = User(0, data.name, data.password)
        userService.addItem(newTodoItem).map(_ => Redirect(routes.UserController.getAll))
      })
  }

  def update(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    UserForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => {
        errorForm.errors.foreach(println)
        Future.successful(BadRequest("Error!"))
      },
      data => {
        val todoItem = User(id, data.name, data.password)
        userService.updateItem(todoItem).map(_ => Redirect(routes.UserController.getAll))
      })
  }

  def delete(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    userService.deleteItem(id) map { res =>
      Redirect(routes.UserController.getAll)
    }
  }

  def processLoginAttempt = Action { implicit request: MessagesRequest[AnyContent] =>
    val errorFunction = { formWithErrors: Form[UserFormData] =>
      // form validation/binding failed...
      BadRequest(views.html.login(formWithErrors, formSubmitUrl))
    }
    val successFunction = { user: UserFormData =>
      // form validation/binding succeeded ...
      val foundUser = userService.lookupUser(user)
      if (foundUser) {
        Redirect(routes.StudentController.getAll)
          .flashing("info" -> "You are logged in.")
          .withSession(Global.SESSION_USERNAME_KEY -> user.name)
      } else {
        Redirect(routes.UserController.showLoginForm)
          .flashing("error" -> "Invalid username/password.")
      }
    }
    val formValidationResult: Form[UserFormData] = UserForm.form.bindFromRequest
    formValidationResult.fold(
      errorFunction,
      successFunction
    )
  }

  private val formSubmitUrl = routes.UserController.processLoginAttempt

  def showLoginForm = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.login(UserForm.form, formSubmitUrl))
  }

}
