package modellingdatawithtraits.calculator

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class CalculatorTest extends AnyFlatSpec with should.Matchers{
  "Calculator" should "Add to numbers and return succeed value" in {
    val expectedResult = Succeed(60)
    val result = Calculator.performOperation(BinaryOperation(20, Addition, 40))
    result shouldBe expectedResult
  }

  it should "subtract to numbers and return succeed value" in {
    val expectedResult = Succeed(12)
    val result = Calculator.performOperation(BinaryOperation(40, Subtraction, 28))
    result shouldBe expectedResult
  }

  it should "multiply to numbers and return succeed value" in {
    val expectedResult = Succeed(100)
    val result = Calculator.performOperation(BinaryOperation(10, Multiplication, 10))
    result shouldBe expectedResult
  }

  it should "divide to numbers and return succeed value" in {
    val expectedResult = Succeed(1)
    val result = Calculator.performOperation(BinaryOperation(10, Division, 10))
    result shouldBe expectedResult
  }

  it should "Fail if the operation is not valid, example divide by 0" in {
    val expectedResult = Fail("Invalid Operation")
    val result = Calculator.performOperation(BinaryOperation(10, Division, 0))
    result shouldBe expectedResult
  }
}
