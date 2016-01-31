package otp

import org.specs2.mutable.Specification

class OneTimePasswordSpec extends Specification {

  val secret = "F3VTHLBOWA6CBAZZ"
  val name = "john.doe@example.com"
  val systemName = "2FA"

  "URI" should {
    "looks like" in {
      OneTimePassword.uri(systemName, name, secret) === "otpauth://totp/2FA%3Ajohn.doe%40example.com?secret=F3VTHLBOWA6CBAZZ&issuer=2FA"
    }
  }
}
