package modellingdatawithtraits

sealed trait Shape{
  def sides: Int
  def perimeter: Double
  def area: Double
  def color: Color
}

final case class Circle(radius: Double, color: Color) extends Shape {
  val sides = 1
  val perimeter =  2 * Math.PI * radius
  val area = Math.PI * radius * radius
}

sealed trait Rectangular extends Shape{
  def width: Double
  def height: Double
  def sides = 4
  def perimeter: Double = (width * 2) + (height *2)
  def area: Double =  width * height
}

final case class Rectangle(width: Double, height: Double, color: Color) extends Rectangular

final case class Square(size: Double, color: Color) extends Rectangular {
  val width = size
  val height = size
}