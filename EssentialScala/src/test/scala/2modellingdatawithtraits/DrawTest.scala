package modellingdatawithtraits

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class DrawTest extends AnyFlatSpec with should.Matchers {

  it should "create Red shape" in {
    val expected = s"A Red Rectangle of width 5.0cm and height 8.0cm"
    val rectangle = Rectangle(5.0, 8.0, Red)
    val result = Draw.apply(rectangle)
    result shouldBe expected
  }

  it should "create Yellow shape" in {
    val expected = s"A Yellow Circle of radius 10.0cm"
    val circle = Circle(10.0, Yellow)
    val result = Draw.apply(circle)
    result shouldBe expected
  }

  it should "create Pink shape" in {
    val expected = s"A Pink Square of size 4.0cm"
    val square = Square(4.0, Pink)
    val result = Draw.apply(square)
    result shouldBe expected
  }
  it should "create Custom shape" in {
    val expected = s"A Square of size 7.0cm"
    val square = Square(7.0, Custom(128,155,240))
    val result = Draw.apply(square)
    result shouldBe expected
  }
}
