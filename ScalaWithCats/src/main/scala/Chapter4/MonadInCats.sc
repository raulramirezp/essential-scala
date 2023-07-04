import cats.Monad

val opt1 = Monad[Option].pure(3)
val lts1 = Monad[List].flatMap(List(1,2,4))(num => List(num.toString))
Monad[Vector].flatMap(Vector(1, 2, 3))(a => Vector(a, a*10))
implicitly[Monad[Option]]

/**
 * Cats provides instances for all the monads in the standard library
 * (Option, List, Vector and so on) via cats.instances
 * Example: import cats.instances.vector._
 */