// Ordering
//  type class instances.
val minOrdering = Ordering.fromLessThan[Int](_ < _)
val maxOrdering = Ordering.fromLessThan[Int](_ > _)
List(3, 4, 2).sorted(minOrdering)
List(3, 4, 2).sorted(maxOrdering)

/*
The type class pattern separates the implementation of functionality
(the type class instance, an Ordering[A] in our example)
from the type the functionality is provided for (the A in an Ordering[A]
 */

// Implicit Values
//implicit val ordered: Ordering[Int] = Ordering.fromLessThan[Int](_ > _)
List(5,4,6,3,7,2,8,9,1).sorted

// Note we didn’t supply an ordering to sorted. Instead, the compiler provides it for us.

/*
*  Declaring Implicit Values
* We can tag any val, var, object or zero-argument def with the implicit keyword,
*  making it a potential candidate for an implicit parameter
* */

/**
 * In Scala, a type class is just a trait. To use a type class we:
• create implementations of that trait, called type class instances;and
• typically we mark the type class instances as implicit values.
 */

implicit val absOrdering: Ordering[Int] = Ordering.fromLessThan[Int](Math.abs(_) < Math.abs(_))
assert(List(-4, -1, 0, 2, 3).sorted(absOrdering) == List(0, -1, 2, 3, -4))
assert(List(-4, -3, -2, -1).sorted(absOrdering) == List(-1, -2, -3, -4))

assert(List(-4, -1, 0, 2, 3).sorted == List(0, -1, 2, 3, -4))
assert(List(-4, -3, -2, -1).sorted == List(-1, -2, -3, -4))

// Rational Orderings
final case class Rational(numerator: Int, denominator: Int)
implicit val rationalOrdered: Ordering[Rational] = Ordering.fromLessThan[Rational](
  (r1, r2) => (r1.numerator.toDouble/ r1.denominator.toDouble) <
    (r2.numerator.toDouble / r2.denominator.toDouble)
)

List(Rational(1, 2), Rational(3, 4), Rational(1, 3)).sorted
assert(List(Rational(1, 2), Rational(3, 4), Rational(1, 3)).sorted
  == List(Rational(1, 3), Rational(1, 2), Rational(3, 4)))
