import cats.data.Writer
import cats.instances.vector._
import cats.syntax.applicative._ // for pure
import cats.syntax.writer._ // for tell

type Logged[A] = Writer[Vector[String], A]
123.pure[Logged]

Vector("msg1", "msg2", "msg3").tell

123.writer(Vector("msg1", "msg2", "msg3"))