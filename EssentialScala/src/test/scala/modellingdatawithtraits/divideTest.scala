package modellingdatawithtraits

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class divideTest extends AnyFlatSpec with should.Matchers{
  "Divide" should "return Finite value when divisor is not 0" in {
    val expected = Finite(4)
    val result = divide(20,5)
    result shouldBe expected
  }

  it should "return Infinite when divisor is 0" in {
    val expected = Infinite
    val result = divide(10, 0)
    result shouldBe expected
  }
}
