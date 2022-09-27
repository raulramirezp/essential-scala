import scala.util.Success

/*
   Product type pattern:
   If A has a b (with type B) and a c (with type C) write

   case class A(b: B, c: C)

  trait A {
    def b: B
    def c: C
  }
 */

/*
* Sum Type:
* Our next pattern is to model data that is two or more distinct cases. We might
* describe this as “A is a B or C”. For example, a Feline is a Cat, Lion, or Tiger;
* a Visitor is an Anonymous or User; and so on.
sealed trait A
final case class B() extends A
final case class C() extends A
 */

/*
Algebraic Data Types
An algebraic data type is any data that uses the above two patterns. In the
functional programming literature, data using the “has-a and” pattern is known
as a product type, and the “is-a or” pattern is a sum type

****************************************
*       |   And         |      or      *
* Is-a  |               |   Sum type   *
* Has-a | Product type  |              *
* **************************************

 */

// Stop on a Dime
// Sum Type ADT
sealed trait TrafficLight
case object Red extends TrafficLight
case object Green extends TrafficLight
case object Yellow extends TrafficLight

// Calculator
sealed trait AlgebraicOperator
case object Addition extends AlgebraicOperator
case object Subtraction extends AlgebraicOperator
case object Multiplication extends AlgebraicOperator
case object Division extends AlgebraicOperator

sealed trait OperationResult
final case class Succeed(value: Int) extends OperationResult
final case class Fail(message: String) extends OperationResult

trait AlgebraicOperation{
  def leftOperand: Int
  def algebraicOperator: AlgebraicOperator
  def rightOperand: Int
}

final case class BinaryOperation(
                                  leftOperand: Int,
                                  algebraicOperator: AlgebraicOperator,
                                  rightOperand: Int
                                ) extends AlgebraicOperation

object Calculator{
  def performOperation(operation: BinaryOperation): OperationResult = operation match {
    case BinaryOperation(leftOperand, Addition, rightOperand) => Succeed(leftOperand + rightOperand)
    case BinaryOperation(leftOperand, Subtraction, rightOperand) => Succeed(leftOperand - rightOperand)
    case BinaryOperation(leftOperand, Multiplication, rightOperand) => Succeed(leftOperand * rightOperand)
    case BinaryOperation(leftOperand, Division, rightOperand) if rightOperand != 0 => Succeed(leftOperand / rightOperand)
    case _ => Fail("Invalid Operation")
  }
}

Calculator.performOperation(BinaryOperation(20, Division, 5))
Calculator.performOperation(BinaryOperation(20, Division, 0))
Calculator.performOperation(BinaryOperation(20, Addition, 5))
Calculator.performOperation(BinaryOperation(20, Subtraction, 5))
Calculator.performOperation(BinaryOperation(20, Multiplication, 5))

//   Water Water Everywhere

sealed trait Source
case object well extends Source
case object spring extends Source
case object tap extends Source

trait Bottled{
  def size: Int
  def source: Source
  def carbonated: Boolean
}

final case class BottledWater(size: Int, source: Source, carbonated: Boolean) extends Bottled