/*
  Case classes are an exceptionally useful shorthand for defining a class, a companion object,
  and a lot of sensible defaults in one go.
 They are ideal for creating lightweight data-holding classes with the minimum of hassle.
 */

case class Person(firstName: String, lastName: String) {
  def name = firstName + " " + lastName
}
// Whenever we declare a case class, Scala automatically generates a class and companion object:
val dave = Person("Dave", "Gurnell") // we have a class
Person // and a companion object too. Person.type

/* ***************************
* Features of a case class
*************************** */

// 1. A field for each constructor argument—we don’t even need to write val
//in our constructor definition, although there’s no harm in doing so.
dave.firstName

// 2. A default toString method
println(dave)

/* 3. Sensible equals, and hashCode methods that operate on the field values in the object.
* Scala’s == operator is different from Java’s—it delegates to equals rather than comparing values on reference identity.
* Scala has an operator called eq with the same behaviour as Java’s ==.
* However, it is rarely used in application code:
*/
Person("Dave", "Gurnell").equals(dave)
Person("Dave", "Gurnell") == dave
Person("Raul", "Gurnell") == dave
Person("Dave", "Gurnell") eq dave // false

// 4. A copy method that creates a new object with the same field values as the current one:
dave.copy(lastName = "Ramirez")

// 5. Case classes implement two traits: java.io.Serializable and scala.Product.

/* *****************************************
* Features of a case class companion object
***************************************** */

// 1. The companion object contains an apply method with the same arguments as the class constructor.
// 2. the companion object also contains code to implement an extractor pattern for use in pattern matching

/* *****************************************
* Case objects
* If you find yourself defining a case class with no constructor arguments you can instead a define a case object
***************************************** */

case object Citizen {
  def firstName = "John"
  def lastName = "Doe"
  def name = firstName + " " + lastName
}

println(Citizen)
Citizen.name

/* *****************************************
*                  Exercises
***************************************** */
case class Cat(colour: String, food: String)

case class Student(firstName: String, lastName: String) {
  def name = firstName + " " + lastName
}

object Student{
  def apply(firstName: String): Student = Student(firstName, "")
}

val student = Student("Raul") // Call the apply method defined explicitly in object
val student2 = Student("Raul", "Ramirez") // Call the apply method defined in the object created with the case class
println(student.name)
println(student2.name)
