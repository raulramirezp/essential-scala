/**************************
* Generic Product type example
 ****************************/
final case class Pair[A,B](one: A, two: B)

val pair = Pair[String, Int]("hi", 2)
pair.one
pair.two

// Scala’s standard library has the generic Tuple Product type

/**************************
// Generic Sum type example
**************************/
sealed trait Sum[A,B]{
  def fold[U](left: A => U, right: B => U):U
}
final case class Left[A,B](value: A) extends Sum[A,B] {
  override def fold[U](left: A => U, right: B => U): U = left(value)
}
final case class Right[A,B](value: B) extends Sum[A,B] {
  override def fold[U](left: A => U, right: B => U): U = right(value)
}

Left[Int, String](1).value
Right[Int, String]("foo").value

val sum: Sum[Int, String] = Right("foo")
sum match {
  case Left(x) => x.toString
  case Right(x) => x
}

// Scala’s standard library has the generic sum type Either
// for two cases, but it does not have types for more cases.

/**************************
// Generic Optional Values
 **************************/

sealed trait Maybe[A]{
  def fold[U](empty: U)(fn: A => U): U = this match {
    case Empty() => empty
    case Full(value) => fn(value)
  }
}
final case class Full[A](value: A) extends Maybe[A]
final case class Empty[A]() extends Maybe[A]

val perhapsEmpty: Maybe[Int] = Empty[Int]
val perhaps: Maybe[Int] = Full(1)

/**************************
// 5.4.6 Exercises
 **************************/

// Folding Maybe
perhaps.fold(0)(_*5)
perhapsEmpty.fold(0)(_*5)

// Folding Sum
sum.fold(i => i,_.concat(" bar"))