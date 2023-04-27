case class Person(name: String, email: String)
case class Cat(name: String, age: Int)

trait HtmlWriter[A] {
  def write(in: A): String
}

implicit class HtmlOps[T](data: T){
  def toHtml(implicit writer: HtmlWriter[T]): String =
    writer.write(data)
}
implicit object PersonWriter extends HtmlWriter[Person] {
  def write(person: Person) = s"<span>${person.name} &lt;${person.
    email}&gt;</span>"
}

implicit object CatWriter extends HtmlWriter[Cat] {
  override def write(cat: Cat): String =
    s"<div> ${cat.name} </div> \n<div> ${cat.age} years old</div>"
}

Person("Cosme", "cosme@fulanito.com").toHtml
Cat("Mauricio", 2).toHtml