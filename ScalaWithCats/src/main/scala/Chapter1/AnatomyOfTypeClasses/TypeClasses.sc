/**
 * The word “class” doesn’t strictly mean class in the Scala or Java sense.
 * There are three important components to the type class pattern:
 * 1) the type class itself.
 * 2) instances for particular types.
 * 3) The methods that use type classes.
 *
    • traits: type classes;
    • implicit values: type class instances;
    • implicit parameters: type class use; and
    • implicit classes: optional utilities that make type classes easier to use.

 A type class use is any functionality that requires a type class instance to work.
 In Scala this means any method that accepts instances of the type class as implicit parameters.

 There are two ways it does this:
 1. Interface Objects
 2. Interface Syntax.
 */


sealed trait Json
final case class JsObject(get: Map[String, Json]) extends Json
final case class JsString(get: String) extends Json
final case class JsNumber(get: Double) extends Json
final case object JsNull extends Json

// The Type Class
trait JsonWriter[A] {
  def write(value: A): Json
}

final case class Person(name: String, email: String)

object JsonWriterInstances{
  implicit val stringWriter: JsonWriter[String] =
    (value: String) => JsString(value)
  implicit val personWriter: JsonWriter[Person] =
    (value: Person) => JsObject(Map(
      "name" -> JsString(value.name),
      "email" -> JsString(value.email)
    ))
}

/**
 * Interface object
 */
object Json {
  def toJson[A](value: A)(implicit w: JsonWriter[A]): Json =
    w.write(value)
}

import JsonWriterInstances._
Json.toJson(Person("Raul", "raul@mail.com"))

/**
 * Interface Syntax
 * You may occasionally see extension methods referred to as “type enrichment” or “pimping”.
 * These are older terms that we don’t use anymore.
 */

object JsonSyntax {
  implicit class JsonOps[A](value: A){
    def toJson(implicit writer: JsonWriter[A]): Json =
      writer write value
  }
}

import JsonSyntax._

Person("Alfonso", "alf@mail.com").toJson
"I'm String".toJson

/**
 * Implicitly method
 */
implicitly[JsonWriter[String]]

/**
 * Working with Implicits
    ° Packaging Implicits
 we can package type class instances in roughly four ways:
 1. by placing them in an object such asJsonWriterInstances;
 2. by placing them in a trait;
 3. by placing them in the companion object of the type class;
 4. by placing them in the companion object of the parameter type.
 */