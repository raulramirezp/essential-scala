package modellingdatawithtraits.calculator

trait AlgebraicOperation {
  def leftOperand: Int
  def algebraicOperator: AlgebraicOperator
  def rightOperand: Int
}

final case class BinaryOperation(
    leftOperand: Int,
    algebraicOperator: AlgebraicOperator,
    rightOperand: Int
) extends AlgebraicOperation
