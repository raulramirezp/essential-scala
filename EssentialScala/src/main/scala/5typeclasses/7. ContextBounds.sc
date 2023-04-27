trait HtmlWriter[A] {
  def write(in: A): String
}

implicit class HtmlOps[T](data: T){
  def toHtml(implicit writer: HtmlWriter[T]): String =
    writer.write(data)
}

case class Person(name: String, email: String)
implicit object PersonWriter extends HtmlWriter[Person] {
  def write(person: Person) = s"<span>${person.name} &lt;${person.
    email}&gt;</span>"
}

def pageTemplate[A](body: A)(implicit writer: HtmlWriter[A]): String = {
  val renderedBody = body.toHtml
  s"<html><head>...</head><body>${renderedBody}</body></html>"
}

pageTemplate[Person](Person("Raul", "Ramirez"))

def pageTemplateContextBound[A: HtmlWriter](body: A): String = {
  val renderedBody = body.toHtml
  s"<html><head>...</head><body>${renderedBody}</body></html>"
}

pageTemplateContextBound(Person("Lina", "lm@mail.com"))

/**
 * The context bound is the notation [A : HtmlWriter] and it expands into the equivalent implicit parameter list
 * in the prior example.
 *
 * Context Bound Syntax
  A context bound is an annota􏰀on on a generic type variable like so:
    [A : Context]
  It expands into a generic type parameter [A] along with an implicit parameter for a Context[A].
 */

/** Implicitly
 * The implicitly method takes no parameters but has a generic type parameters. It returns the implicit
 * matching the given type, assuming there is no ambiguity.
 */

val implicitPersonWriter  = implicitly[HtmlWriter[Person]]
implicitPersonWriter.write(Person("Laura", "Marcela"))