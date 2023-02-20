/*
  "In the discussion we described an Adder as an object representing a
  computation—a bit like having a method that we can pass around as a value.
  This is such a powerful concept that Scala has a fully blown set of language
  features for creating objects that behave like computations. These objects are
  called functions, and are the basis of functional programming."
  from: https://books.underscore.io/essential-scala/essential-scala.pdf

   function application syntax
 */
class Adder(amount: Int){
  def apply(in: Int): Int = amount + in
}

val adder = new Adder(6)
adder(6) // shorthand for adder.apply(6)
adder.apply(2)

def func(): Unit = println("function 0 ")
val fun0 = () => ()
val fun1: Int => String = x => x.toString


/*
  Companion objects:
  Sometimes we want to create a method that logically belongs to a class but
  is independent of any particular object. In Java we would use a static method
  for this, but Scala has a simpler solution that we’ve seen already: singleton
  objects.
 */

class Timestamp(val seconds: Long)

object Timestamp {
  def apply(hours: Int, minutes: Int, seconds: Int): Timestamp =
    new Timestamp(hours*60*60 + minutes*60 + seconds)
}

Timestamp(1, 1, 1).seconds

/*
  Scala has two namespaces: a space of type names and a space of value names.
  This separation allows us to name our class and companion object the same thing without conflict.

 s. When reading a block of code it is important
to know which parts refer to a class or type and which parts refer to a singleton
object or value.
 */

/* *****************************************
*                  Exercises
***************************************** */

// Friendly Person Factory
// type
class Person(val firstName: String, val lastName: String){
  import Person.fieldOnObject
  val anotherField = "Another field in the class"
  override def toString: String = s"Person($firstName,$lastName)"
  def showName(): Unit = println(s"Name is: ${firstName}")

  def showValuesInObject(): Unit = println(s"field on object from class: ${fieldOnObject}")
}

//value - singleton
object Person{
  val fieldOnObject = "Value in object"
  def apply(fullName: String): Person = {
    val name = fullName.split(" ")
    new Person(name(0), name(1))
  }

  def showMessage(): Unit = println(s"Can't access to non-static members")
}

val person = Person("Raul Ramirez")
println(person)
// object
Person.showMessage()
// class
person.showName()
person.showValuesInObject()
