package modellingdatawithtraits.calculator

sealed trait AlgebraicOperator

case object Addition extends AlgebraicOperator
case object Subtraction extends AlgebraicOperator
case object Multiplication extends AlgebraicOperator
case object Division extends AlgebraicOperator
