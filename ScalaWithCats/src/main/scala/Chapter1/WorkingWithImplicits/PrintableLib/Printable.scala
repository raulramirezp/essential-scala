package Chapter1.WorkingWithImplicits.PrintableLib

trait Printable[A] {
  def format(value: A): String
}

object PrintableInstances {
  implicit val stringFormat: Printable[String] = { (value: String) =>
    s"The string $value"
  }

  implicit val intFormat: Printable[Int] = { (value: Int) =>
    s"The int $value"
  }
}

// Interface object
object Printable {
  def format[A](value: A)(implicit printable: Printable[A]): String =
    printable.format(value)

  def print[A](value: A)(implicit printable: Printable[A]): Unit =
    println(format(value))
}

// Interface syntax
object PrintableSyntax {
  implicit class PrintableOps[A](value: A) {
    def format(implicit printable: Printable[A]): String =
      printable format value

    def print(implicit printable: Printable[A]): Unit =
      println(format)
  }
}
