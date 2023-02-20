package modellingdatawithtraits.calculator

object Calculator {
  def performOperation(operation: BinaryOperation): OperationResult = operation match {
    case BinaryOperation(leftOperand, Addition, rightOperand) => Succeed(leftOperand + rightOperand)
    case BinaryOperation(leftOperand, Subtraction, rightOperand) => Succeed(leftOperand - rightOperand)
    case BinaryOperation(leftOperand, Multiplication, rightOperand) => Succeed(leftOperand * rightOperand)
    case BinaryOperation(leftOperand, Division, rightOperand) if rightOperand != 0 => Succeed(leftOperand / rightOperand)
    case _ => Fail("Invalid Operation")
  }
}
