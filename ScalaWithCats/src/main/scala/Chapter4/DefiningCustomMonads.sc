import scala.annotation.tailrec

import cats.{Applicative, Monad}

/** We can define a Monad for a custom type by providing implementations of
  * three methods: flatMap, pure and tailRecM
  */

val optionMonad = new Monad[Option] {
  override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] =
    fa flatMap f

  @tailrec
  override def tailRecM[A, B](a: A)(f: A => Option[Either[A, B]]): Option[B] =
    f(a) match {
      case None => None
      case Some(Left(value)) => tailRecM(value)(f)
      case Some(Right(value)) => Some(value)
    }

  override def pure[A](x: A): Option[A] = Option(x)
}

optionMonad.tailRecM(100000)(a => if (a == 0) None else Some(Left(a - 1)))

import cats.syntax.applicative._
//Exercise: Branching out Further with Monads
import cats.syntax.flatMap._
import cats.syntax.functor._
sealed trait Tree[+A]
object Tree {
  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)

  def leaf[A](value: A): Tree[A] = Leaf(value)

  implicit val treeMonad: Monad[Tree] = new Monad[Tree] {
    override def pure[A](value: A): Tree[A] = Leaf(value)

    override def flatMap[A, B](tree: Tree[A])(f: A => Tree[B]): Tree[B] =
      tree match {
        case Branch(left, right) => Branch(flatMap(left)(f), flatMap(right)(f))
        case Leaf(value) => f(value)
      }

    //@tailrec
    override def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] =
      flatMap(f(a)) {
        case Left(value) =>
          tailRecM(value)(f)
        case Right(value) =>
          Leaf(value)
      }

  }
}

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
final case class Leaf[A](value: A) extends Tree[A]

val treeList: Tree[List[Int]] = Tree.branch(
  Branch(Leaf(List(1, 2, 4)), Leaf(List(4, 5, 6))),
  Leaf(List(7, 8, 9))
)
treeList.flatMap(list => Applicative[Tree].pure(list.sum))

treeList.map(_.map(_ + 1))

Tree.branch(Leaf(100), Leaf(200)).
  flatMap(x => Branch(Leaf(x - 1), Leaf(x + 1)))