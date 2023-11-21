import cats.data.{ EitherT, OptionT }
import cats.implicits.catsSyntaxApplicativeId
import scala.concurrent.Future

type ListOption[A] = OptionT[List, A]

val result1: ListOption[Int] = OptionT(List(Option(2)))
val result2: ListOption[Int] = 3.pure[ListOption]

for{
  num1 <- result1
  num2 <- result2
} yield num1 + num2

val optionT: OptionT[List, Int] = OptionT[List, Int](List(Some(2), Some(3), Some(4)))
optionT.flatMap(x => OptionT.when(x % 2 == 0)(x))


import cats.instances.future._ // for Monad
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import cats.syntax.either._

/**
 * For example, let’s create a Future of an Either of Option.
 * Once again we build this from the inside out with an OptionT of an EitherT of Future.
 * However, we can’t define this in one line because EitherT has three type parameters:
 */
// , let’s create a Future of an Either of Option
// EitherT[F[_], E, A]

type FutureEither[A] = EitherT[Future, String, A] //Future[Either[String,A]]
type FutureEitherOption[A] = OptionT[FutureEither, A] //Future[Either[String,Option[A]]]

val feo = 1.pure[FutureEitherOption]

val futureEitherOr: FutureEitherOption[Int] =
  for {
    a <- 10.pure[FutureEitherOption]
    b <- 32.pure[FutureEitherOption]
  } yield a + b


// Alias Either to a type constructor with one parameter:
type ErrorOr[A] = Either[String, A]
// Build our final monad stack using OptionT:
type ErrorOrOption[A] = OptionT[ErrorOr, A]

val errorStack2 = 32.pure[ErrorOrOption]
errorStack2.value.map(_.getOrElse(-1))

// Use patterns
import cats.data.Writer
type Logged[A] = Writer[List[String], A]
// Methods generally return untransformed stacks:
def parseNumber(str: String): Logged[Option[Int]] = util.Try(str.toInt).toOption match {
  case Some(num) => Writer(List(s"Read $str"), Some(num))
  case None      => Writer(List(s"Failed on $str"), None)
}

def addAll(a: String, b: String, c: String): Logged[Option[Int]] = { import cats.data.OptionT
  val result = for {
    a <- OptionT(parseNumber(a))
    b <- OptionT(parseNumber(b))
    c <- OptionT(parseNumber(c))
  } yield a + b + c
  result.value
}

val result1 = addAll("1", "2", "3")
val result2 = addAll("1", "a", "3")

// Exercise: Monads: Transform and Roll Out