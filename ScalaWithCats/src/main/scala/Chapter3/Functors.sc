import scala.collection.immutable.Queue
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

val future: Future[String] =
  Future(123).map(n => n + 1).map(n => n * 2).map(n => s"${n}!")
Await.result(future, 1.second)

// Example 2: Future is not referentially transparent
import scala.util.Random
val future1 = {
  // Initialize Random with a fixed seed:
  val r = new Random(0L)
  // nextInt has the side-effect of moving to
  // the next random number in the sequence:
  val x = Future(r.nextInt)
  for {
    a <- x
    b <- x
  } yield (a, b)
}

val future2 = {
  val r = new Random(0L)
  for {
    a <- Future(r.nextInt)
    b <- Future(r.nextInt)
  } yield (a, b)
}
val result1 = Await.result(future1, 1.second)
val result2 = Await.result(future2, 1.second)

// single argument functions are also functors.

import cats.syntax.functor._

val func1: Int => Double =
  (x: Int) => x.toDouble

val func2: Double => Double =
  (y: Double) => y * 2

(func1 map func2)(1)
(func1 andThen func2)(1)
func2(func1(1))

val func = ((x: Int) => x.toDouble)
  .map(x => x + 1)
  .map(x => x * 2)
  .map(x => s"{$x!}")

func(123)

/** Definition of a Functor Formally, a functor is a type F[A] with an operation
  * map with type (A => B) => F[B]. The general type chart is shown in Figure
  * 3.4.
  *
  * [o] map o => * -> [*] F[A] A => B F[B] Figure 3.4.
  *
  * Functor Laws
  *   1. Identity: calling map with the identity function is the same as doing
  *      nothing: fa.map(a => a) == fa 2. Composition: mapping with two
  *      functions f and g is the same as mapping with f and then mapping with
  *      g: fa.map(g(f(_))) == fa.map(f).map(g)
  */

/** > Higher Kinds and Type Constructors Kinds are like types for types. They
  * describe the number of “holes” in a type. We distinguish between regular
  * types that have no holes and “type constructors” that have holes we can fill
  * to produce types.
  */
import cats.Functor
import cats.instances.list._

val list1 = List(1, 2, 3)
val list2 = Functor[List].map(list1)(_ * 2)

val option1 = Option(123)
val option2 = Functor[Option].map(option1)(_.toString)

val fun: Int => Int = x => x * x
val lifted = Functor[Option].lift(fun)
lifted(Option(5))

Functor[List].as(list2, "As")

def doMath[F[_]](start: F[Int])(implicit functor: Functor[F]): F[Int] =
  start.map(n => n + 1 * 2)

doMath(List(1, 2, 3))
doMath(Option(20))
doMath(Future(2))

case class Box[A](value: A)
implicit val boxFunctor: Functor[Box] = new Functor[Box] {
  override def map[A, B](fa: Box[A])(f: A => B): Box[B] =
    Box(f(fa.value))
}

Box("S").map(_.concat(" cat"))
Box(1).map(_ + 9)
Box(1).as("B")

// Exercises
sealed trait Tree[+A]
object Tree {
  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] =
    Branch(left, right)
  def leaf[A](value: A): Tree[A] =
    Leaf(value)

  implicit val treeFunctor: Functor[Tree] = new Functor[Tree] {
    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] =
      fa match {
        case Branch(left, right) =>
          Branch(map(left)(f), map(right)(f))
        case Leaf(value) => Leaf(f(value))
      }
  }
}
final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
final case class Leaf[A](value: A) extends Tree[A]


val tree: Tree[Int] = Branch(Branch(Leaf(1), Leaf(2)), Leaf(3))
println(tree)
println(tree.map(_ * 2))

val treeList = Tree.branch(Branch(Leaf(List(1,2,4)), Leaf(List(4,5,6))), Leaf(List(7,8,9)))
treeList.map(_.map(_ +1))
