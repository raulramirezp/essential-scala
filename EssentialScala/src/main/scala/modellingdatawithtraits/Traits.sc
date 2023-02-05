/*
Traits are templates for creating classes, in the same way that classes are templates for creating objects
 */
import modellingdatawithtraits.{Circle, Custom, Draw, Pink, Rectangle, Rectangular, Red, Shape, Square, Yellow}

import java.util.Date

trait Visitor {
  def id: String // Unique id assigned to each user

  def createdAt: Date // Date this user first visited the site

  // How long has this visitor been around?
  def age: Long = new Date().getTime - createdAt.getTime
}


case class Anonymous(
                      id: String,
                      createdAt: Date = new Date()
                    ) extends Visitor

case class User(
                 id: String,
                 email: String,
                 createdAt: Date = new Date()
               ) extends Visitor


User("1234", "sdsd", new Date())
val anonymousUser = Anonymous("1234")
anonymousUser.createdAt
anonymousUser.age

/* *****************************************
*                  Exercises
***************************************** */
trait Feline {
  def colour: String

  def sound: String
}

trait BigCats extends Feline{
  override val sound: String = "roar"
}

case class Tiger(colour: String) extends BigCats

case class Lion(colour: String, maneSize: Int) extends BigCats

case class Panther(colour: String) extends BigCats

case class Cat(colour: String, food: String) extends Feline{
  val sound: String = "meow"
}

val kosianfiro = Cat("Gray", "monello")
kosianfiro.sound


/*
When we mark a trait as sealed we must define all of its subtypes in the same
file. Once the trait is sealed, the compiler knows the complete set of subtypes
and will warn us if a pattern matching expression is missing a case:

 */

Draw.apply(Circle(10.0, Yellow()))
Draw.apply(Rectangle(3.0, 4.0, Red()))
Draw.apply(Square(4.0, Pink()))
val square = Square(4.0, Custom(128,128,128))
val circle = Circle(10.0, Custom(0,153,0))
val rectangle = Rectangle(3.0, 4.0, Custom(204,255,204))
val rectangleCustomRed = Rectangle(3.0, 4.0, Custom(255,0,0))
Draw.apply(rectangleCustomRed)
println(square.color.name)
println(square.color.tone)

println(circle.color.name)
println(circle.color.tone)

println(rectangle.color.name)
println(rectangle.color.tone)

println(rectangleCustomRed.color.name)
println(rectangleCustomRed.color.tone)

sealed abstract class Animal(val name: String)

case class Cat(age: Int, override val name: String) extends Animal(name)