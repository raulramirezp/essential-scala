// 7.4.4 Exercises
trait Equal[A] {
  def equal(v1: A, v2: A): Boolean
}

object Equal {
  def apply[A](implicit equal: Equal[A]): Equal[A] =
    equal
}
case class Person(name: String, email: String)

object EmailEqual extends Equal[Person] {
  def equal(v1: Person, v2: Person): Boolean =
    v1.email == v2.email
}

object NameEmailEqual extends Equal[Person] {
  def equal(v1: Person, v2: Person): Boolean =
    v1.email == v2.email && v1.name == v2.name
}

object NameEqual extends Equal[Person] {
  override def equal(v1: Person, v2: Person): Boolean =
    v1.name == v2.name
}


object Eq {
  def apply[A](valueA: A, valueB: A)(implicit equal: Equal[A]): Boolean =
    equal.equal(valueA, valueB)
}

Eq(Person("Noel", "noel@example.com"), Person("Noel", "noel@example. com"))(NameEqual)

// Package up the different Equal implementations as implicit values ...
object EmailEqualImplicit {
  implicit object EmailEqual extends Equal[Person] {
    def equal(v1: Person, v2: Person): Boolean =
      v1.email == v2.email
  }
}

object NameEmailEqualImplicit {
  implicit object NameEmailEqual extends Equal[Person] {
    def equal(v1: Person, v2: Person): Boolean =
      v1.email == v2.email && v1.name == v2.name
  }
}

object Examples {
  def byNameAndEmail = {
    import NameEmailEqualImplicit._
    Equal[Person].equal(Person("Noel", "noel@example.com"), Person("Noel", "noel@example. com"))
  }

  def byEmail = {
    import EmailEqualImplicit._
    Equal[Person].equal(Person("Noel", "noel@example.com"), Person("Noel", "noel@example.com"))
  }
}

Examples.byNameAndEmail
Examples.byEmail