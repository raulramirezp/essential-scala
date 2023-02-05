package sequencingcomputations

sealed trait Result[A]
final case class Success[A](value: A) extends Result[A]
final case class Failure[A](reason: String) extends Result[A]