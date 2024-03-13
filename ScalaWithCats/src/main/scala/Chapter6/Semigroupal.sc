/**
 * cats.Semigroupal is a type class that allows us to combine contexts
 * If we have two object of type F[A] and F[B], semigroupal[F] allows us to combine them
 * to form a F[(A,B)].
 * trait Semigroupal[F[_]] {
 *   def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]
 * }
 *
 * Semigroupal Laws:
 *   the product method must be associative.
 *   product(a, product(b, c)) == product(product(a, b), c)
 */

/**
 * While Semigroup allows us to join values,
 * Semigroupal allows us to join contexts.
 */
import cats.arrow.FunctionK
import cats.{Functor, Monad, Semigroupal}
import cats.data.EitherT
import cats.implicits.{catsSyntaxEitherId, toFlatMapOps, toFunctorOps}
import cats.instances.option._ // for Semigroupal
Semigroupal[Option].product(Some(123), Some("abc")) //Some((123,abc))
Semigroupal[Option].product(Some(21), None)
Semigroupal[Option].product(None, None)
Semigroupal[Option].product(None, Some("abc"))

// tuple
Semigroupal.tuple3(Option(1), Option(2), Option(3))
Semigroupal.tuple3(Option(1), Option(2), Option.empty[Int])

//map
Semigroupal.map3(Option(1), Option(2), Option(3))(_ + _ + _)
Semigroupal.map2(Option(1), Option.empty[Int])(_ + _)

//Apply Syntax
import cats.syntax.apply._     // for tupled and mapN
(Option(123), Option("abc"), Option(true)).tupled
(Option(1), Option(3), Option(5), Option(7)).mapN(_ + _ + _ + _)

/**
 * Internally mapN uses the Semigroupal to extract the values from the Option
 * and the Functor to apply the values to the function. this syntax is type checked
 */

/**
 * Future: The semantics for Future provide parallel as opposed to sequential execution
 */
import cats.Semigroupal
import cats.instances.future._ // for Semigroupal
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
val futurePair = Semigroupal[Future].
  product(Future("Hello"), Future(123))
Await.result(futurePair, 1.second)

// Example 2
case class Cat(
                name: String,
                yearOfBirth: Int,
                favoriteFoods: List[String]
              )

val futureCat = (
  Future("Garfield"),
  Future(1978),
  Future(List("Lasagne"))
).mapN(Cat.apply)
Await.result(futureCat, 1.second)

/**
 * List
 * Combining Lists with Semigroupal produces some potentially unexpected results. We might expect code like the
 * following to zip the lists, but we actually get the cartesian product of their elements:
 *
 * import cats.Semigroupal
 * import cats.instances.list._ // for Semigroupal
 */


Semigroupal[List].product(List(1, 2), List(3, 4))

/**
 * Either. Product implements the same fail‐fast behaviour as flatMap
 */
import cats.instances.either._ // for Semigroupal
type ErrorOr[A] = Either[Vector[String], A]
Semigroupal[ErrorOr].product(
  Left(Vector("Error 1")),
  Left(Vector("Error 2"))
)

/**
 * Semigroupal Applied to Monads
 * The reason for the surprising results for List and Either is that they are both monads.
 * If we have a monad we can implement product as follows.


import cats.Monad
import cats.syntax.functor._ // for map
import cats.syntax.flatMap._ // for flatmap
def product[F[_]: Monad, A, B](fa: F[A], fb: F[B]): F[(A,B)] =
  fa.flatMap(a =>
    fb.map(b =>
      (a, b))
  )
It would be very strange if we had different semantics for product depending on how we implemented it.
To ensure consistent semantics, Cats’ Monad (which extends Semigroupal) provides a standard definition of product
in terms of map and flatMap as we showed above.

Even our results for Future are a trick of the light. flatMap provides sequential ordering,
so product provides the same. The parallel execution we observe occurs because our constituent Futures start running
before we call product. This is equivalent to the classic create‐then‐flatMap pattern:

val a = Future("Future 1")
val b = Future("Future 2")
for {
  x <- a
  y <- b
} yield (x, y)
*/
