sealed trait Shape
case class Circle(radius: Double) extends Shape

// Covariance
val circles: List[Circle] = List(Circle(1.2), Circle(2.0))
val shapes: List[Shape] = circles

/** Contravariance
trait Json
trait F[-A]
trait JsonWriter[-A] {
  def write(value: A): Json
}

val shape: Shape = ???
val circle: Circle = ???
val shapeWriter: JsonWriter[Shape] = ???
val circleWriter: JsonWriter[Circle] = ???
def format[A](value: A, writer: JsonWriter[A]): Json = writer.write(value)

 “Which combinations of value and writer can I pass to format?” :
We can write a Circle with either writer because all Circles are Shapes.
Conversely, we can’t write a Shape with circleWriter because not all Shapes are Circles.

 This relationship is what we formally model using contravariance.
JsonWriter[Shape] is a subtype of JsonWriter[Circle] because Circle is a subtype of Shape.
This means we can use shapeWriter anywhere we expect to see a JsonWriter[Circle].
*/
