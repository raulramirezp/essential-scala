import scala.annotation.tailrec

//Pandora's Box
// The syntax [A] is called a type parameter.
final case class Box[A](value: A)

Box(1)
Box("String value").value

def genericFunction[B](input: B): B = input

genericFunction(1)
genericFunction(Box("pandora"))

sealed trait Result[A]
final case class Success[A](value: A) extends Result[A]
final case class Failure[A](reason: String) extends Result[A]

/*
 Invariant Generic Sum Type Pattern
 If A of type T is a B or C write

 sealed trait A[T]
 final case class B[T]() extends A[T]
 final case class C[T]() extends A[T]
 */

/*
sealed trait IntList
case object End extends IntList
final case class Pair(head: Int, tail: IntList) extends IntList

Change the name to LinkedList and make it generic in the type of data stored in the list.
 */

sealed trait LinkedList[A] {
  def length: Int = this match {
    case End() => 0
    case Pair(_, tail) => 1 + tail.length
  }

  def contains(value: A): Boolean = this match {
    case End() => false
    case Pair(head, _) if head == value => true
    case Pair(_, tail) => tail.contains(value)
  }

  def apply(index: Int): Result[A] =
/*    def aux(current: Int, linkedList: LinkedList[A]): A =
      linkedList match {
        case End() => throw new Exception("index out of bounds")
        case Pair(head, tail) =>
          if (current == index) head
          else aux(current + 1, tail)
    }
    aux(0, this)*/
  this match {
    case End() => Failure("Index out of bounds")
    case Pair(head, tail) =>
      if(index == 0) Success(head)
      else tail(index-1)
  }

}
final case class End[A]() extends LinkedList[A]
final case class Pair[A](head: A, tail: LinkedList[A]) extends LinkedList[A] {}

// Implement length
val example = Pair(1, Pair(2, Pair(3, End())))
println(s"The length of example is ${example.length}")
assert(example.length == 3)
assert(example.tail.length == 2)
assert(End().length == 0)

// Implement contains
val example = Pair(6,Pair(1, Pair(2, Pair(3, End()))))
assert(example.contains(3) == true)
assert(example.contains(4) == false)
assert(End().contains(0) == false)
example.contains(6)
// This should not compile
// example.contains("not an Int")

// Implement  a method apply that returns the nth item in the list
val example = Pair(1, Pair(2, Pair(3, End())))
assert(example(0) == Success(1))
assert(example(1) == Success(2))
assert(example(2) == Success(3))
assert(example(3) == Failure("Index out of bounds"))
example(3)