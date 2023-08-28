import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt // for tell
import scala.concurrent.{Await, Future}

import cats.data.Writer
import cats.instances.vector._
import cats.syntax.applicative._
import cats.syntax.writer._

type Logged[A] = Writer[Vector[String], A]
123.pure[Logged]

println(Vector("msg1", "msg2", "msg3").tell)

val writer = 123.writer(Vector("msg1", "msg2", "msg3"))
// Extracting values from writer monad
println(writer.value)
println(writer.written)
println(writer.run)

val writer1 = for {
  a <- 10.pure[Logged]
  _ <- Vector("a", "b", "c").tell
  b <- 32.writer(Vector("x", "y", "z"))
} yield a + b
/*writer1.mapWritten(_.map(_.toUpperCase()))
  println(s"${20.pure[Logged]
    .flatMap{value =>
      Vector("a", "b", "c").tell
        .flatMap(_ => 35.writer(Vector("x", "y", "z")))
        .map(value2 => value + value2)
  }
  }")*/

//Exercise
def slowly[A](body: => A) =
  try body
  finally Thread.sleep(100)
/*def factorial(n: Int): Logged[Int] =
  slowly(
  if (n == 0) 1.pure[Logged]
  else factorial(n - 1).map(_ * n)
  ).flatMap(ans => Vector(s"fact of $n is: ${ans}").tell.map(_=>ans))*/
def factorial(n: Int): Logged[Int] =
  for {
    ans <- slowly(
      if (n == 0) 1.pure[Logged]
      else factorial(n - 1).map(_ * n)
    )
    _ <- Vector(s"fact of $n is: ${ans}").tell
  } yield ans

val res = Await.result(
  Future.sequence(
    Vector(
      Future(factorial(5)),
      Future(factorial(6))
    )
  ),
  5.seconds
)

res.foreach(println(_))
