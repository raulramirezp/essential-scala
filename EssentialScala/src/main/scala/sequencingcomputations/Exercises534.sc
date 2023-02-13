// 5.3.4.1 Tree

sealed trait Tree[A]{

  def fold[B](node: (B, B) => B, leaf: A => B): B
}
case class Node[A](left: Tree[A], right: Tree[A]) extends Tree[A] {
  override def fold[B](node: (B, B) => B, leaf: A => B): B =
    node(left.fold(node, leaf), right.fold(node,leaf))
}
case class Leaf[A](value: A) extends Tree[A] {
  override def fold[B](node: (B, B) => B, leaf: A => B): B =
    leaf(value)
}

val tree = Node(
  Node(Leaf(2),Node(Leaf(4), Leaf(4))),
  Node(Leaf(3),Node(Leaf(5), Leaf(6))))

tree.fold[Int]((b,c) => b + c, i => i)

val tree2: Tree[String] =
  Node(Node(Leaf("To"), Leaf("iterate")),
    Node(Node(Leaf("is"), Leaf("human,")),
      Node(Leaf("to"), Node(Leaf("recurse"), Leaf("divine")))))

tree2.fold[String]((nl, nr) => nl.concat(s" $nr"), i => i)