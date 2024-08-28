import cats.{Functor, Semigroupal}

/**
 * Semigroupals aren’t mentioned frequently in the wider functional programming literature. They provide a
 * subset of the functionality of a related type class called an applicative functor (“applicative” for short).
 *
 * Semigroupal and Applicative effectively provide alternative encodings of the same notion of joining contexts
 *
 * Cats models applicatives using two type classes.
 *  1. cats.Apply: adds the ap method that applies a parameter to a function within a context.
 *  2. cats.Applicative: adds the pure method
 */

trait Apply[F[_]] extends Semigroupal[F] with Functor[F] {
  def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]
  def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] =
    ap(map(fa)(a => (b: B) => (a, b)))(fb)
}

trait Applicative[F[_]] extends Apply[F] {
  def pure[A](a: A): F[A]
}

/**
 * Applicative is related to Apply as Monoid is related to Semigroup.
 */