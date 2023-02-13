import sequencingcomputations.Success

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
  def fold[U](left: A => U, right: B => U):U = this match {
    case Left(value) => left(value)
    case Right(value) => right(value)
  }

  def map[U](fn: B => U): Sum[A,U] = this match {
    case Left(value) => Left[A,U](value)
    case Right(value) => Right(fn(value))
  }

  def flatMap[U](fn: B => Sum[A,U]): Sum[A,U] = this match {
    case Left(value) => Left[A,U](value)
    case Right(value) => fn(value)
  }
}
final case class Left[A,B](value: A) extends Sum[A,B]
final case class Right[A,B](value: B) extends Sum[A,B]

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

  def map[U](fn: A => U): Maybe[U] = this match {
    case Empty() => Empty[U]()
    case Full(value) => Full(fn(value))
  }

  def mapVersion2[U](fn: A => U): Maybe[U] =
    this.flatMap(value => Full(fn(value)))
  def flatMap[U](fn: A => Maybe[U]): Maybe[U] = this match {
    case Empty() => Empty[U]()
    case Full(value) => fn(value)
  }
}
final case class Full[A](value: A) extends Maybe[A]
final case class Empty[A]() extends Maybe[A]

val perhapsEmpty: Maybe[Int] = Empty[Int]()
val perhaps: Maybe[Int] = Full(5)

/**************************
// 5.4.6 Exercises
 **************************/

// Folding Maybe
perhaps.fold(0)(_*5)
perhapsEmpty.fold(0)(_*5)

// Folding Sum
sum.fold(i => i,_.concat(" bar"))

// mapping Maybe
perhaps.map(_.toString)
perhaps.flatMap(value => Full((value-2).toString))

/**************************
// 5.5.4 Exercises
 **************************/

// Mapping Maybe
perhaps.mapVersion2(_.toString)

val list: List[Maybe[Int]] = List(Full(3), Full(2), Full(1))

list.map(maybe => maybe.flatMap[Int] { x => if (x % 2 == 0) Full(x) else Empty() })

//5.5.4.4 Sum
sum.map(_ => 1)
val failure = Left("An error has occurred")
failure.map(_ => 4)

sum.flatMap{str =>
  if(str == "foo") Right(10)
  else Left[Int,Int](-1)
}

Right("bar").flatMap{str =>
  if(str == "foo") Right[Int,Int](10)
  else Left[Int,Int](-1)
}
