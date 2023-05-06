class B {
  def bar() = "This is the best method ever"
}

class A

implicit def aToB(in: A): B = new B()

println(new A().bar())

/**
 * Las conversiones implicitas pueden ser peligrosas, en el sentido de que puede generar comportamientos inespetados
 * en nuestro código, el siguiente código es un ejemplo de ello
 *
 * implicit def intToBoolean(in: Int): Boolean = in == 0
 * if(1) println("true") else println("false")
 * if(0) println("true") else println("false")
 */

object IntImplicit {
  class IntOps(n: Int) {
    def yeah(): Unit =
      times(_ => println("Oh yeah!"))

    //Times
    def times(fn: Int => Unit): Unit =
      for (i <- 0 until n)
        fn(i)
  }

  implicit def intToIntOps(value: Int) = new IntOps(value)
}

import IntImplicit._

import java.util.Date

5.yeah()

//JSON Serialisation
sealed trait JsValue {
  def stringify: String
}

final case class JsObject(values: Map[String, JsValue]) extends JsValue {
  def stringify = values
    .map { case (name, value) => "\"" + name + "\":" + value.stringify
    }
    .mkString("{", ",", "}")
}

final case class JsString(value: String) extends JsValue {
  def stringify = "\"" + value.replaceAll("\\|\"", "\\\\$1") + "\""
}

val obj = JsObject(Map("foo" -> JsString("a"), "bar" -> JsString("b"), "baz" -> JsString("c")))
obj.stringify

//Type class
trait JsWriter[A] {
  def write(value: A): JsValue
}

object JsUtil {
  def toJson[A](value: A)(implicit writer: JsWriter[A]): JsValue =
    writer.write(value)
}

implicit class JsUtilEnriched[A](value: A) {
  def toJson(implicit writer: JsWriter[A]): JsValue =
    writer.write(value)
}

implicit object StringWriter extends JsWriter[String] {
  def write(value: String) = JsString(value)
}

implicit object DateWriter extends JsWriter[Date] {
  def write(value: Date) = JsString(value.toString)
}
// visitor clases

import java.util.Date

sealed trait Visitor {
  def id: String

  def createdAt: Date

  def age: Long = new Date().getTime() - createdAt.getTime()
}

final case class Anonymous(
                            id: String,
                            createdAt: Date = new Date()
                          ) extends Visitor

object Anonymous {
  implicit object JsonWriter extends JsWriter[Anonymous] {
    override def write(visitor: Anonymous): JsValue =
      JsObject(Map("id" -> visitor.id.toJson, "createdAt" -> visitor.createdAt.toJson))
  }
}

case class User(
                 id: String,
                 email: String,
                 createdAt: Date = new Date()
               ) extends Visitor

object User {
  implicit object JsonWriter extends JsWriter[User] {
    def write(user: User): JsValue = {
      JsObject(Map("id" -> user.id.toJson,
        "email" -> user.email.toJson,
        "createdAt" -> user.createdAt.toJson))
    }
  }
}

implicit object VisitorWriter extends JsWriter[Visitor] {
  def write(value: Visitor) = value match {
    case anon: Anonymous => JsUtil.toJson(anon)
    case user: User => JsUtil.toJson(user)
  }
}

JsUtil.toJson(User("Raul Ramirez", "Raul@mail.com")).stringify

val visitors: Seq[Visitor] = Seq(Anonymous("001", new Date), User("003 ", "dave@xample.com", new Date))
visitors.map(JsUtil.toJson(_))

Anonymous("002", new Date).toJson.stringify
