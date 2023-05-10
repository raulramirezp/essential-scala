package Chapter1.WorkingWithImplicits

import PrintableLib.Printable

object CatPrintableInstances {
  implicit val catPrintable: Printable[Cat] = { (cat: Cat) =>
    s"${cat.name} is a ${cat.age} year-old ${cat.color} cat"
  }
}
