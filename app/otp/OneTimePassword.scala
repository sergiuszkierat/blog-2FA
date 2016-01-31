package otp

import org.jboss.aerogear.security.otp.Totp
import org.jboss.aerogear.security.otp.api.Base32

object OneTimePassword {
  def secret = Base32.random

  def verify(secret: String, otp: String) = {
    val totp = new Totp(secret)
    totp.verify(otp)
  }

  def uri(system: String, name: String, secret: String) = {
    val totp = new Totp(secret)

    val uriWithoutIssuer = totp.uri(s"$system:$name")
    s"$uriWithoutIssuer&issuer=$system"
  }
}
