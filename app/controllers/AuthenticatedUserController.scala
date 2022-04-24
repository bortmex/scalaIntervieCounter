package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class AuthenticatedUserController @Inject()(
    cc: ControllerComponents,
    authenticatedUserAction: AuthenticatedUserAction
) extends AbstractController(cc) {

    def logout = authenticatedUserAction { implicit request: Request[AnyContent] =>
        // docs: “withNewSession ‘discards the whole (old) session’”
        Redirect(routes.StudentController.getAll)
            .flashing("info" -> "Вы не зашли, пожалуйста залогиньтесь.")
            .withNewSession
    }

}

