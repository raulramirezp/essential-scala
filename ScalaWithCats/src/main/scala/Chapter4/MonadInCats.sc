import cats.{Id, Monad}

val opt1 = Monad[Option].pure(3)
val lts1 = Monad[List].flatMap(List(1,2,4))(num => List(num.toString))
Monad[Vector].flatMap(Vector(1, 2, 3))(a => Vector(a, a*10))
implicitly[Monad[Option]]

/**
 * Cats provides instances for all the monads in the standard library
 * (Option, List, Vector and so on) via cats.instances
 * Example: import cats.instances.vector._
 */

import cats.syntax.functor._ // for map
import cats.syntax.flatMap._ // for flatMap
def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
  a.flatMap(x => b.map(y => y*y + x*x))

sumSquare(List(1, 2, 3), List(4, 5))

/**
 * The Identity Monad.
 * This would allow us to abstract over monadic and non‐monadic code
 */


val intIdA = Monad[Id].pure(3)
val intIdB = Monad[Id].pure(5)

//intIdA.flatMap(x => intIdB.map(_ + x))
for{
  x <- intIdA
  y <- intIdB
} yield x+y

//Exercise: Monadic Secret Identities
def pure[A](value: A): Id[A] = value

pure(2)

def map[A,B](idMonad: Id[A])(fn: A => B): Id[B] =
  fn(idMonad)

map(5)(_ * 2)
def flatMap[A, B](idMonad: Id[A])(fn: A => Id[B]): Id[B] =
  fn(idMonad)

flatMap(10)(_ + 10)

// About Either
import cats.syntax.either._
val a: Either[String, Int] = 3.asRight

//Transforming Eithers
(-1).asRight.ensure("Must be non-negative")(_ > 0)

"error".asLeft[Int].recover{
  case "error" => -1
}

"error".asLeft[Int].recoverWith {
  case "error" => Right(-1)
}

6.asRight[String].bimap(_.reverse, _ * 7)
"Foo Bar".asLeft[Int].bimap(_.reverse, _ * 7)

123.asRight[String].swap

//Error Handling and MonadError
import cats.MonadError
type ErrorOr[A] = Either[String, A]
val monadError = MonadError[ErrorOr, String]
val success: ErrorOr[Int] = monadError.pure(5)

//raiseError is like the pure method for Monad except that it creates an instance representing a failure:
val failure = monadError.raiseError("Badness")

monadError.handleErrorWith(failure){
  case "Badness" => monadError.pure("It is ok")
  case _ => monadError.raiseError("Is not ok")
}

monadError.handleError(failure){
  case "Badness" => 1
  case _ => -1
}
monadError.ensure((-1).asRight)("Must be positive")(_ >= 0 )
monadError.ensure((1).asRight)("Must be positive")(_ >= 0 )

// syntax for raiseError and handleErrorWith
import cats.syntax.applicative.catsSyntaxApplicativeId // for pure
import cats.syntax.applicativeError._ // for raiseError

val success2 = 42.pure[ErrorOr]
val failure2 = "Badness".raiseError[ErrorOr, Int]
failure2.handleErrorWith{
  case "Badness" => 123.pure[ErrorOr]
  case _ => "Invalid value".raiseError
}
success2.ensure("Number should be odd")(_ %2 != 0)

//exercise
import cats.instances.try_._ // for MonadError
import cats.syntax.monadError._ // for ensure
def validateAdult[F[_]](age: Int)(implicit me: MonadError[F, Throwable ]): F[Int] =
  age.pure[F].ensure(new IllegalArgumentException("Age must be greater than or equal to 18"))(_ >= 18)

validateAdult(16)
validateAdult(18)

type ExceptionOr[A] = Either[Throwable, A]
validateAdult[ExceptionOr](-1)