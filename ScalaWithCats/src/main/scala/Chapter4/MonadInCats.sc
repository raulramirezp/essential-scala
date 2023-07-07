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