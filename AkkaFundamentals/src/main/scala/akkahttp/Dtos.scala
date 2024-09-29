package akkahttp

import java.util.UUID
import scala.collection.immutable

final case class User(name: String, age: Int, countryOfResidence: String)

final case class Users(users: immutable.Seq[User])

final case class UserCreated(userID: String)