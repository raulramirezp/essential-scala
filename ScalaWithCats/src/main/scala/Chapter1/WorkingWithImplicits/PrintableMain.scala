package Chapter1.WorkingWithImplicits

import PrintableLib.Printable
import PrintableLib.PrintableInstances._
import CatPrintableInstances._
import PrintableLib.PrintableSyntax.PrintableOps
object PrintableMain extends App {
  val name: String = "Raul Ramirez"
  val number: Int = 2

  Printable.print(name)
  Printable.print(number)

  val mauricio: Cat = Cat("Mauricio", 2, "black")
  val cat = Cat("Garfield", 41, "ginger and black")
  Printable.print(mauricio)

  cat.format
  cat.print
}
