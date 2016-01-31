package controllers

import otp.OneTimePassword
import play.api.Play.current
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.data.validation.Constraints
import play.api.data.{Form, Mapping}
import play.api.i18n.Messages.Implicits._
import play.api.mvc._

class Application extends Controller {
  val secret = "F3VTHLBOWA6CBAZZ"
  val name = "john.doe@example.com"
  val systemName = "2FA"

  val oneTimePassword: Mapping[String] = of[String] verifying Constraints.pattern(
    """\d{6}""".r,
    "constraint.otp",
    "error.otp")

  val otpForm = Form(
    single(
      "oneTimePassword" -> oneTimePassword
    )
  )

  def index = Action {
    Ok(views.html.index(otpForm))
  }

  def configureOTP = Action {
    Ok(views.html.configure(OneTimePassword.uri(systemName, name, secret)))
  }

  def verifyPassword() = Action { implicit request =>
    otpForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.index(formWithErrors))
      },
      otpData => {
        play.Logger.debug(s"Delivered otp is '$otpData'")
        if (OneTimePassword.verify(secret, otpData)) {
          play.Logger.info(s"Passed!")
          Redirect(routes.Application.secured())
        }
        else {
          play.Logger.error(s"Failed!")
          BadRequest(views.html.index(otpForm.withGlobalError("Bad OTP")))
        }
      }
    )
  }

  def secured() = Action {
    Ok(views.html.secured())
  }
}
