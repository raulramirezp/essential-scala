/**
 *  is a monad that allows us to sequence operations that de‐ pend on some input.
 *  Instances of Reader wrap up functions of one argument,
 *  providing us with useful methods for composing them.
 *
 *  One common use for Readers is dependency injection
 */
import cats.data.Reader
import cats.implicits.catsSyntaxApplicativeId
final case class Cat(name: String, favoriteFood: String)
// We can create a Reader[A, B] from a function A => B using the Reader.apply constructor:
val catName: Reader[Cat, String] = Reader(cat => cat.name)
//We can extract the function again using the Reader's run method and call it using apply as usual
catName.run(Cat("Patikalvo", "Monello"))
//Composing
val greetKitty: Reader[Cat, String] =
  catName.map(name => s"Hello $name")
greetKitty.run(Cat("Patikalvo", "Monello"))

val feedKitty: Reader[Cat, String] =
  Reader(cat => s"Have a nice bowl of ${cat.favoriteFood}")

val greetAndFeed: Reader[Cat, String] =
  for {
    greet <- greetKitty
    feed  <- feedKitty
  } yield s"$greet. $feed."

greetAndFeed(Cat("Patikalvo", "Monello"))

//Exercise: Hacking on Readers
final case class Db(
                     usernames: Map[Int, String],
                     passwords: Map[String, String]
                   )
type DbReader[A] = Reader[Db, A]

def findUsername(userId: Int): DbReader[Option[String]] =
  Reader(db => db.usernames.get(userId))

def checkPassword(
                   username: String,
                   password: String
                 ): DbReader[Boolean] =
  Reader(db => db.passwords.get(username).contains(password))

def checkLogin(
                userId: Int,
                password: String
              ): DbReader[Boolean] =
  for{
    maybeUsername <- findUsername(userId)
    isAuthorized <- maybeUsername.fold(false.pure[DbReader])(checkPassword(_, password))
  } yield isAuthorized

val users = Map(
  1 -> "dade",
  2 -> "kate",
  3 -> "margo"
)
val passwords = Map(
  "dade"  -> "zerocool",
  "kate"  -> "acidburn",
  "margo" -> "secret"
)
val db = Db(users, passwords)
checkLogin(1, "zerocool").run(db)
checkLogin(4, "davinci").run(db)

/**
 * Readers are most useful in situations where:
• we are constructing a program that can easily be represented by a function;
• we need to defer injection of a known parameter or set of parameters;
• we want to be able to test parts of the program in isolation.
 */