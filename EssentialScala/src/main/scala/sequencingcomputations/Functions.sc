import sequencingcomputations._
sealed trait IntLinkedList {
  def length: Int = this.fold(0)((_, b) => 1 + b)

  def sum: Int = this.fold(0)(_ + _)

  def product: Int = this.fold(1)(_ * _)

  def contains(value: Int): Boolean = this match {
    case End => false
    case Pair(head, _) if head == value => true
    case Pair(_, tail) => tail.contains(value)
  }

  def apply(index: Int): Result[Int] =
    this match {
      case End => Failure("Index out of bounds")
      case Pair(head, tail) =>
        if(index == 0) Success[Int](head)
        else tail(index-1)
    }

  def double: IntLinkedList = this.map(_ * 2)
  def map(function: Int => Int): IntLinkedList =
    this match {
      case End => End
      case Pair(head, tail) => Pair(function(head), tail.map(function))
    }
  def fold(initialValue: Int)(function: (Int, Int) => Int): Int =
    this match {
      case End => initialValue
      case Pair(head, tail) => function(head, tail.fold(initialValue)(function))
    }

}
case object End extends IntLinkedList
final case class Pair(head: Int, tail: IntLinkedList) extends IntLinkedList {}

// Implement length
val example = Pair(12,Pair(1, Pair(2, Pair(3, End))))
println(s"Length ${example.length}")
println(s"sum ${example.sum}")
println(s"product ${example.product}")
println(s"double ${example.double}")