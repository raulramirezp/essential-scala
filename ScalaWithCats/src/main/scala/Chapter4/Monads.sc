/**
 * A monad is a mechanism for sequencing computations.
 * Every monad is also a functor
 * monadic behaviour is formally captured in two operations:
 * • pure, of type A => F[A]
 * • flatMap, of type (F[A], A => F[B]) => F[B].
 */
trait Monad[F[_]] {
  def pure[A](value: A): F[A]
  def flatMap[A,B](value: F[A])(func: A => F[B]): F[B]

  def map[A, B](value: F[A])(func: A => B): F[B] =
    flatMap(value)(x => pure(func(x)))
}

/**
 * Monad Laws
 * • Left identity: pure(a).flatMap(function) == function(a)
 * • Right identity: m.flatMap(pure) == m
 * • Associativity: m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))
 */