package controllers

import entity.Student
import formz.{StudentForm, StudentFormData}
import play.api.libs.json._
import play.api.mvc._
import services.StudentService

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class StudentController @Inject()(
                                   cc: MessagesControllerComponents,
                                   studentService: StudentService
                                 ) extends MessagesAbstractController(cc) {

  implicit val studentFormat = Json.format[Student]

  private val logoutUrl = routes.AuthenticatedUserController.logout

  def getAll = Action.async { implicit request: Request[AnyContent] =>
    val allOptionsFuture: Future[Seq[Student]] = studentService.listAllItems
    val maybeUsername = request.session.get(entity.Global.SESSION_USERNAME_KEY)
    allOptionsFuture map {
      options =>
        maybeUsername match {
          case None => Ok(views.html.students(access = false, null, null, options))
          case Some(u) => Ok(views.html.students(access = true, logoutUrl, routes.StudentController, options))
        }
    }
  }

  def add = Action.async { implicit request: Request[AnyContent] =>
    StudentForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => {
        errorForm.errors.foreach(println)
        Future.successful(BadRequest("Error!"))
      },
      data => {
        val todoItem = Student(0, data.name, data.intervieCounter, data.javaRushLevel, data.chanceToComplete)
        studentService.addItem(todoItem).map(_ => Redirect(routes.StudentController.getAll))
      })
  }

  def editStudent(id: Long) = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val student: Future[Option[Student]] = studentService.getItem(id)
    student map {
      student =>
        val maybeUsername = request.session.get(entity.Global.SESSION_USERNAME_KEY)
        maybeUsername match {
          case None => {
            //TODO потом
            Ok("Error")
          }
          case Some(u) => {
            val studentBD = student.get
            val studentFormData = StudentFormData(studentBD.name, studentBD.intervieCounter, studentBD.javaRushLevel, studentBD.chanceToComplete)
            Ok(views.html.editStudent(StudentForm.form.fill(studentFormData), routes.StudentController.update(student.get.id)))
          }
        }
    }
  }

  def addStudent = Action.async { implicit request: MessagesRequest[AnyContent] =>
    Future {
      Ok(views.html.editStudent(StudentForm.form.fill(StudentFormData("", 0, 0, 0)), routes.StudentController.add))
    }
  }

    def update(id: Long) = Action.async { implicit request: MessagesRequest[AnyContent] =>
      StudentForm.form.bindFromRequest.fold(
        // if any error in submitted data
        errorForm => {
          errorForm.errors.foreach(println)
          Future.successful(BadRequest("Error!"))
        },
        data => {
          val todoItem = Student(id, data.name, data.intervieCounter, data.javaRushLevel, data.chanceToComplete)
          studentService.updateItem(todoItem).map(_ => Redirect(routes.StudentController.getAll))
        })
    }

    def deleteStudent(id: Long) = Action.async { implicit request: MessagesRequest[AnyContent] =>
      studentService.deleteItem(id) map { res =>
        Redirect(routes.StudentController.getAll)
      }
    }

  }
