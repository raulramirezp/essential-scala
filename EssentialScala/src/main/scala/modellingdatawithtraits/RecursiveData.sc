import scala.annotation.tailrec

/*    Recursive Data
A particular use of algebraic data types that comes up very o[en is defining
recursive data. This is data that is defined in terms of itself,
 */

sealed trait IntList{

  def length2(): Int = {
    @tailrec
    def lengthTR(list: IntList, size: Int = 0): Int = list match {
      case End => size
      case Pair(head, tail) => lengthTR(tail, size + 1)
    }
    lengthTR(this)
  }


  def length(): Int = this match {
    case End => 0
    case Pair(_, tail) => tail.length() + 1
  }

  def product(): Int = this match {
    case End => 1
    case Pair(head, tail) => head * tail.product()
  }

  def double: IntList = this match {
    case End => End
    case Pair(head, tail) => Pair(head * 2, tail.double)
  }
}

case object End extends IntList
final case class Pair(head: Int, tail: IntList) extends IntList

val d = End
val c = Pair(3, d)
val b = Pair(2, c)
val a = Pair(1, b)

def sum(list: IntList): Int = list match {
  case End => 0
  case Pair(head, tail) => head + sum(tail)
}

@tailrec
def sumTR(list: IntList, acc: Int = 0): Int = list match {
  case End => acc
  case Pair(head, tail) => sumTR(tail, head + acc)
}

val example = Pair(1, Pair(2, Pair(3, End)))
sum(example)
assert(sum(example) == 6)
assert(sum(example.tail) == 5)
assert(sum(End) == 0)

sumTR(example)
assert(sumTR(example) == 6)
assert(sumTR(example.tail) == 5)
assert(sumTR(End) == 0)

assert(example.length == 3)
assert(example.tail.length == 2)
assert(End.length == 0)

example.length

assert(example.length2 == 3)
assert(example.tail.length2 == 2)
assert(End.length2 == 0)
example.length2

assert(example.product == 6)
assert(example.tail.product == 6)
assert(End.product == 1)

assert(example.double == Pair(2, Pair(4, Pair(6, End))))
assert(example.tail.double == Pair(4, Pair(6, End)))
assert(End.double == End)
example.double

// The Forest of Trees
sealed trait Tree{
  def sum: Int = this match {
    case Leaf(value) => value
    case Node(left, right) => left.sum + right.sum
  }

  def double: Tree = this match {
    case Leaf(value) => Leaf(value * 2)
    case Node(left, right) => Node(left.double, right.double)
  }
}

final case class Leaf(value: Int) extends Tree
final case class Node(left: Tree, right: Tree) extends Tree

val root = 5
val left = 10
val right = 15
val tree = Node(Leaf(root), Node(Leaf(left), Leaf(right)))
tree.sum
tree.double