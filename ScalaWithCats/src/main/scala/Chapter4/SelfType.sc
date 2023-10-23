import jdk.internal.org.jline.utils.ShutdownHooks.Task

import java.time.{Instant, ZoneId}
import java.time.temporal.ChronoUnit

/**
 * Self-types are a way to declare that a trait must be mixed into another trait,
 * even though it doesn’t directly extend it. That makes the members of the dependency available without imports.
 */

trait User{
  def username: String
  val joinDate: Instant
}

trait X{
  user: User =>
    def post(message: String) =
      println(s"$username: $message")

    def getInfo(): String =
      s"$username register since ${joinDate.atZone(ZoneId.of("UTC"))} "
}

class VerifiedUser(val username_ : String) extends X with User{
  override def username = s"Verify account of $username_"

  override val joinDate: Instant = Instant.now().truncatedTo(ChronoUnit.DAYS)
}

val patikalvo = new VerifiedUser("Patikalvito21")
patikalvo.post("Mi primer post en X antes twitter")
println(patikalvo.getInfo())


