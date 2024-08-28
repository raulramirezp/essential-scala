
/***********   Parallel **************/
/**
In the previous section we saw that when call product on a type that has a Monad instance we get sequential semantics.
This makes sense from the point‐ of‐view of keeping consistency with implementations of product in terms of flatMap and map.
However it’s not always what we want.
The Parallel type class, and its associated syntax, allows us to access alternate semantics for certain monads.
 */

import cats.{NonEmptyParallel, Semigroupal}
import cats.arrow.FunctionK
import cats.implicits.catsSyntaxTuple2Semigroupal
import cats.syntax.parallel._ // for parTupled

type ErrorOr[A] = Either[List[String], A]
val error1: ErrorOr[Int] = Left(List("Error 1"))
val error2: ErrorOr[Int] = Left(List("Error 2"))
Semigroupal[ErrorOr].product(error1, error2)

(error1, error2).tupled

// To collect all the errors we simply replace tupled with its “parallel” version called parTupled.



(error1, error2).parTupled
implicitly(NonEmptyParallel)

val success1: ErrorOr[Int] = Right(1)
val success2: ErrorOr[Int] = Right(2)
val addTwo = (x: Int, y: Int) => x + y

(error1, error2).parMapN(addTwo)
(success1, success2).parMapN(addTwo)

/**
 * Let’s dig into how Parallel works. The definition below is the core of Parallel.
 *
 * trait Parallel[M[_]] {
    type F[_]
    def applicative: Applicative[F]
    def monad: Monad[M]
    def parallel: ~>[M, F]

    ~> is a type alias for https://typelevel.org/cats/api/cats/arrow/FunctionK.html and is what performs the
     conversion from M to F. A normal function A => B converts values of type A to values of type B.
     Remember that M and F are not types; they are type constructors. (F[_], M[_]).
     A FunctionK M ~> F is a function from a value with type M[A] to a value with type F[A].
  }
 */
object optionToList extends FunctionK[Option, List] {
  def apply[A](fa: Option[A]): List[A] =
    fa match {
      case Some(value) => List(value)
      case None => Nil
    }
}

optionToList(Some(1))
optionToList(None)

/**
 *
So in summary, Parallel allows us to take a type that has a monad instance and convert it to some related type
that instead has an applicative (or semigroupal) instance.
 We’ve seen the case above where the related applicative for Either allows
for accumulation of errors instead of fail‐fast semantics.
Now we’ve seen Parallel it’s time to finally learn about Applicative.
 */

// List does have a Parallel instance, and it zips the List insted of creating the cartesian product as Semigroup product does
val list1 = List(1,2,3,4,5)
val list2 = List(6,7,8,9,10)
(list1, list2).parTupled