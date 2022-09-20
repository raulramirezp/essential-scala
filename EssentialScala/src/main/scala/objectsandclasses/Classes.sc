/*
  Simple class definition
 */
class Person{
  val firstName = "Raul"
  val lastName = "Ramirez"

  def name = firstName + " " + lastName
}

/*
unlike an object name, we
cannot use a class name in an expression. A class is not a value, and there is a
different namespace in which classes live.
 */
// Person;  Fails wit: not found: value Person
val person = new Person
Integer.toHexString(person.hashCode())
person

/*
  Using a constructor
 */
class Dog(name: String, age: Int){
  override
  def toString: String = s"Name: $name, Age: $age"
}

val myDog = new Dog("Luna", 5)

/*
  Using: Keyword Parameters
 */

class Person(name: String = "name", lastName: String = "last name"){
  override
  def toString: String = s"Name: $name, LastName: $lastName"
}

val personWithAllDefaultValues = new Person()
val personWithSomeDefaultValues = new Person(name = "Cosme")
val personWithoutDefaultValues = new Person(name = "Cosme", "fulanito")
println(personWithAllDefaultValues)
println(personWithSomeDefaultValues)
println(personWithoutDefaultValues)

// Keyword parameters and default values in functions
def greet(name: String = "to", lastName: String = "you") =
  println(s"Greetings $name $lastName")

greet() // Using default values
greet(lastName = "Ramirez") // Using one default value
greet(name = "Raul", lastName = "Ramirez") // Passing all parameters

/*
  Scala’s Type Hierarchy
                                   Any
                                   /  \
                              AnyVal  AnyRef = java.lang.Object
                                /       \
   (Int, Double, Boolean, Unit, ...)   (java.lang.String, Array[T], All Java, All Scala)


                          \ /
                          null
                      \    /
                       Nothing
 */

val valueType: AnyVal = 3
val anyVal: AnyVal = println()
val valueRef: AnyRef = "String object"

// types at the bottom of the hierarchy
// Nothing: is the type of throw expressions
def badness: Nothing = throw new Exception("Error")

// Null is the type of the value null
def otherbadness = null

/*
  Exercises
 */
