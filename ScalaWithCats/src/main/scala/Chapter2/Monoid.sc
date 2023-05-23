import cats.implicits.catsSyntaxSemigroup
import cats.kernel.Monoid
import cats.kernel.instances.int._
/**
 * A Monoid for a type A is:
 * • An operation combine with type (A, A) => A
 * • An element empty of type A
 *
 * In Scala code
 *  trait Monoid[A]{
 *    def combine(x: A, y: A): A
 *    def empty: A
 *  }
 *
 *   monoids must formally obey several laws.
 *   For all values x, y, and z, in A, combine must be associative and empty must be an identity element:
 */

def associativeLaw[A](x: A, y: A, z: A)(implicit monoid: Monoid[A]) =
  monoid.combine(x, monoid.combine(y, z)) == monoid.combine(monoid.combine(x,y), z)

def identityLaw[A](x: A)(implicit monoid: Monoid[A]): Boolean =
  (monoid.combine(x, monoid.empty) == x) && (monoid.combine(monoid.empty, x) == x)

implicit val monoid: Monoid[Int] = Monoid[Int]

associativeLaw[Int](1, 2, 3)
identityLaw(4)