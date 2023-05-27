import cats.Monoid
import cats.Semigroup
import cats.implicits.catsSyntaxSemigroup
//import cats.syntax.semigroup._


Monoid[String].combine("Hello", " cats monoid")
Monoid[String].empty

Semigroup[Int].combine(1, Monoid[Int].combine(2,4))

val monoid: Monoid[String] = Monoid[String]
monoid.combine("I have ", " instance for string")

val a = Option(22)
val b = Option(20)
Monoid[Option[Int]].combine(a, b)
Monoid[Option[Int]].empty

// Interface syntax to combine method
"I'm a " combine "Semigroup"
"I'm also a " |+| "Semigroup"

val intResult = 1 |+| 2 |+| Monoid[Int].empty

// Exercise: Adding All The Things
def add[A: Monoid](items: List[A]): A =
  items.foldRight(Monoid.empty)(_ |+| _)


add(List(1,2,3,4))
add(List(Option(1),Option(2),Option(3),Option(4)))

case class Order(totalCost: Double, quantity: Double)
object Order{
  implicit val monoid: Monoid[Order] = new Monoid[Order]{
    override def empty: Order = Order(0, 0)

    override def combine(x: Order, y: Order): Order =
      Order(x.totalCost |+| y.totalCost, x.quantity |+| y.quantity)
  }
}
val order1 = Order(5.2, 3)
val order2 = Order(6.8, 4)
add(List(order1, order2))