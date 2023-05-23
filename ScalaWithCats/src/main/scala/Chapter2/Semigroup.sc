/**
 * A semigroup is just the combine part of a monoid, without the empty part.
 *
 * if we restrict ourselves to non‐empty sequences and positive integers,
 * we are no longer able to define a sensible empty element.
 * Cats has a NonEmptyList data type that has an implementation of Semigroup but no implementation of Monoid.
 *
 *  trait Semigroup[A] {
 *    def combine(x:A, y: A): A
 *  }
 *
 *  trait Monoid[A] extends Semigroup[A] {
 *    def empty: A
 *  }
 *
 */

// Exercise: The Truth About Monoids
trait Semigroup[A] {
  def combine(x: A, y: A): A
}
trait Monoid[A] extends Semigroup[A] {
  def empty: A
}
object Monoid {
  def apply[A](implicit monoid: Monoid[A]): Monoid[A] =
    monoid
}

object MonoidInstances{
  object BooleanAndOpt {
    implicit val and: Monoid[Boolean] = new Monoid[Boolean] {
      override def empty = true

      override def combine(x: Boolean, y: Boolean): Boolean = x && y
    }
  }

  object BooleanOrOpt {
    implicit val or: Monoid[Boolean] = new Monoid[Boolean] {
      override def empty = false

      override def combine(x: Boolean, y: Boolean): Boolean = x || y
    }
  }

  object BooleanXOROpt {
    implicit val xor: Monoid[Boolean] = new Monoid[Boolean] {
      override def empty = false

      override def combine(x: Boolean, y: Boolean): Boolean = (x && !y) || (!x && y)
    }
  }

  object BooleanXNOROpt {
    implicit val xnor: Monoid[Boolean] = new Monoid[Boolean] {
      override def empty = true

      override def combine(x: Boolean, y: Boolean): Boolean = (x && y) || (!x && !y)
    }
  }
}

def associativeLaw[A](x: A, y: A, z: A)(implicit monoid: Monoid[A]): Boolean =
  monoid.combine(x, monoid.combine(y, z)) == monoid.combine(monoid.combine(x,y), z)

def identityLaw[A](x: A)(implicit monoid: Monoid[A]): Boolean =
  (monoid.combine(x, monoid.empty) == x) && (monoid.combine(monoid.empty, x) == x)

associativeLaw(true, true, true)(MonoidInstances.BooleanAndOpt.and)
associativeLaw(true, true, false)(MonoidInstances.BooleanAndOpt.and)
associativeLaw(true, false, false)(MonoidInstances.BooleanAndOpt.and)
associativeLaw(false, false, false)(MonoidInstances.BooleanAndOpt.and)
associativeLaw(true, false, true)(MonoidInstances.BooleanAndOpt.and)
associativeLaw(false, true, true)(MonoidInstances.BooleanAndOpt.and)
identityLaw(true)(MonoidInstances.BooleanAndOpt.and)
identityLaw(false)(MonoidInstances.BooleanAndOpt.and)

associativeLaw(true, true, true)(MonoidInstances.BooleanOrOpt.or)
associativeLaw(true, true, false)(MonoidInstances.BooleanOrOpt.or)
associativeLaw(true, false, false)(MonoidInstances.BooleanOrOpt.or)
associativeLaw(false, false, false)(MonoidInstances.BooleanOrOpt.or)
associativeLaw(true, false, true)(MonoidInstances.BooleanOrOpt.or)
associativeLaw(false, true, true)(MonoidInstances.BooleanOrOpt.or)
identityLaw(true)(MonoidInstances.BooleanOrOpt.or)
identityLaw(false)(MonoidInstances.BooleanOrOpt.or)

identityLaw(true)(MonoidInstances.BooleanXOROpt.xor)
identityLaw(false)(MonoidInstances.BooleanXOROpt.xor)

identityLaw(true)(MonoidInstances.BooleanXNOROpt.xnor)
identityLaw(false)(MonoidInstances.BooleanXNOROpt.xnor)

import MonoidInstances._
BooleanXOROpt.xor.combine(false, false)
BooleanXOROpt.xor.combine(false, true)
BooleanXOROpt.xor.combine(true, false)
BooleanXOROpt.xor.combine(true, true)
println("XNOR")
println(BooleanXNOROpt.xnor.combine(false, false))
println(BooleanXNOROpt.xnor.combine(false, true))
println(BooleanXNOROpt.xnor.combine(true, false))
println(BooleanXNOROpt.xnor.combine(true, true))

//Exercise: All Set for Monoids