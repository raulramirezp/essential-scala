//Drinking the Kool Aid

implicit class ExtraIntMethods(value: Int) {
  def yeah(): Unit =
    times(_ => println("Oh yeah!"))

  //Times
  def times(fn: Int => Unit): Unit =
    for(i <- 0 until value)
      fn(i)
}

5.yeah
val negative = -10
negative.yeah()
3.times(i => println(s"Look - it's the number $i!"))

//Easy Equality
trait Equal[A] {
  def equal(v1: A, v2: A): Boolean
}

object Equal{
  def apply[A](implicit equal: Equal[A]): Equal[A] =
    equal

  implicit class ExtensionMethod[A](in: A){
    def ===(comparator: A)(implicit compare: Equal[A]): Boolean =
      compare.equal(in, comparator)
  }
}
implicit object StringComparator extends Equal[String]{
  def equal(v1: String, v2: String): Boolean =
    v1.toLowerCase equals v2.toLowerCase
}


import Equal._

"abcd".===("ABCD")
"abcd".===("ABCd")


case class Car(brand: String, model: String, year: Int)
object Car{
  implicit object CarComparator extends Equal[Car] {
    override def equal(v1: Car, v2: Car): Boolean = {
      (v1.model equals v2.model) && (v1.brand equals v2.brand) &&
        (v1.year equals v2.year)
    }
  }
}

val car1 = Car("Tesla", "model 3", 2020)
val car2 = Car("Tesla", "model 3", 2020)
val car3 = Car("Renault", "Stepway", 2021)


car1 === car2
car1 === car3