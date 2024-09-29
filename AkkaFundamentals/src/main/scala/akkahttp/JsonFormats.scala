package akkahttp

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import java.util.UUID

//#json-formats

object JsonFormats  {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val userJsonFormat: RootJsonFormat[User] = jsonFormat3(User.apply)
  implicit val usersJsonFormat: RootJsonFormat[Users] = jsonFormat1(Users.apply)
  implicit val usersResponseJsonFormat: RootJsonFormat[UserCreated] = jsonFormat1(UserCreated.apply)
}
//#json-formats
