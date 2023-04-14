/**
 * A type class is a trait with at least one type variable.
 * The type variables specify the concrete types the type class instances are defined for.
 * Methods in the trait usually use the type variables.
 *
 * trait ExampleTypeClass[A] {
 * def doSomething(in: A): Foo
 * }
 *
 example:
 trait HtmlWriter[A] {
  def toHtml(in: A): String
  }

  object PersonWriter extends HtmlWriter[Person] {
    def toHtml(person: Person) =
      s"${person.name} (${person.email})"
  }

  object ObfuscatedPersonWriter extends HtmlWriter[Person] {
    def toHtml(person: Person) = s"${person.name} (${person.email.replaceAll("@", " at ")})"
  }
 */

case class Person(name: String, email: String)

trait Equal[A]{
  def equal(first: A, second: A): Boolean
}

object ComparePersonByEmail extends Equal[Person] {
  override def equal(first: Person, second: Person): Boolean =
    first.email == second.email
}

object ComparePersonByNameAndEmail extends Equal[Person] {
  override def equal(first: Person, second: Person): Boolean =
    first.name == second.name && first.email == second.email
}


trait HtmlWriter[A] {
  def write(in: A): String
}

object HtmlWriter {
  def apply[A](implicit writer: HtmlWriter[A]): HtmlWriter[A] =
    writer
}

object HtmlUtil {
  def htmlify[A](data: A)(implicit writer: HtmlWriter[A]): String =
    writer.write(data)
}
implicit object PersonWriter extends HtmlWriter[Person] {
  def write(person: Person) = s"<span>${person.name} &lt;${person.
    email}&gt;</span>"
}

implicit object ApproximationWriter extends HtmlWriter[Int] { def write(in: Int): String =
  s"It's definitely less than ${((in / 10) + 1) * 10}" }

HtmlUtil.htmlify(Person("Raul", "rramirez@mail.com"))(PersonWriter)

HtmlUtil.htmlify(2)

HtmlWriter[Person].write(Person("Cosme", "cosme@fulanito.com"))

/**
 * Type Class Interface Pattern
   If the desired interface to a type class TypeClass is exactly the methods defined on the type class trait,
   define an interface on the companion object using a no-argument apply method like

   object TypeClass {
    def apply[A](implicit instance: TypeClass[A]): TypeClass[A] =
    instance
  }
 */