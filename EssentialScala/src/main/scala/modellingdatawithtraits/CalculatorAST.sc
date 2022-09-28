
sealed trait EvalResult
final case class Succeed(value: Double) extends EvalResult
final case class Failure(message: String) extends EvalResult

sealed trait Expression {
  def eval(): EvalResult = this match {
    case Number(value) => Succeed(value)
    case Addition(left, right) =>
      (left.eval(), right.eval()) match {
        case (Succeed(valueA), Succeed(valueB)) => Succeed(valueA + valueB)
        case (Failure(message),Succeed(_)) => Failure(message)
        case (Succeed(_), Failure(message)) => Failure(message)
        case (Failure(messageA), Failure(messageB)) => Failure(s"${messageA} and ${messageB}")
      }
    case Subtraction(left, right) =>
      (left.eval(), right.eval()) match {
      case (Succeed(valueA), Succeed(valueB)) => Succeed(valueA - valueB)
      case (Failure(message), Succeed(_)) => Failure(message)
      case (Succeed(_), Failure(message)) => Failure(message)
      case (Failure(messageA), Failure(messageB)) => Failure(s"${messageA} and ${messageB}")
    }
    case Division(left, right) =>
      (left.eval(), right.eval()) match {
        case (Succeed(valueA), Succeed(valueB)) if valueB != 0 => Succeed(valueA / valueB)
        case (Succeed(_), Succeed(_))  => Failure("Division by zero")
        case (Failure(message), Succeed(_)) => Failure(message)
        case (Succeed(_), Failure(message)) => Failure(message)
        case (Failure(messageA), Failure(messageB)) => Failure(s"${messageA} and ${messageB}")
      }
    case SquareRoot(expression) => expression.eval() match {
      case Succeed(value) if value >= 0 => Succeed(Math.sqrt(value))
      case _ => Failure("Square root of negative number")
    }
  }
}
final case class Number(value: Double) extends Expression
final case class Addition(left: Expression, right: Expression) extends Expression
final case class Subtraction(left: Expression, right: Expression) extends Expression
final case class Division(left: Expression, right: Expression) extends Expression
final case class SquareRoot(left: Expression) extends Expression

val numberA = Number(20)
val numberB = Number(10)
val expression = Subtraction(Addition(numberA, numberB), Number(5))
expression.eval()
assert(Addition(SquareRoot(Number(-1.0)), Number(2.0)).eval == Failure("Square root of negative number"))
assert(Addition(SquareRoot(Number(4.0)), Number(2.0)).eval == Succeed(4.0))
assert(Division(Number(4), Number(0)).eval == Failure("Division by zero"))
Addition(SquareRoot(Number(-1.0)), Number(2.0)).eval
Addition(SquareRoot(Number(4.0)), Number(2.0)).eval
Division(Number(4), Number(0)).eval
Division(Number(4), Subtraction(expression, Number(25))).eval