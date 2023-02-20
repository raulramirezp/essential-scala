import scala.util.Try
// 5.6.6.2 Calculator Again

sealed trait Sum[+A, +B]{

  def map[U](fn: B => U): Sum[A,U] = this match {
    case Success(value) => Success(fn(value))
    case Failure(error) => Failure(error)
  }
  def flatMap[U, AA >: A](fn: B => Sum[AA,U]): Sum[AA,U] = this match {
    case Success(value) => fn(value)
    case Failure(error) => Failure(error)
  }

  def fold[U](error: A => U)(success: B => U): U = this match {
    case Failure(value) => error(value)
    case Success(value) => success(value)
  }

}
case class Failure[A](error: A) extends Sum[A,Nothing]
case class Success[B](value: B) extends Sum[Nothing,B]

sealed trait Expression {
  def eval(): Sum[String, Double] = this match {
    case Number(value) => Success(value)
    case Addition(left, right) =>
      left.eval().flatMap( res1 =>  right.eval().map(res2 => res1 + res2))

    case Subtraction(left, right) =>
      left.eval()
        .flatMap( res1 =>  right.eval().map(res2 => res1 - res2))
    case Division(left, right) =>
      right.eval().flatMap { res1 =>
        if(res1 != 0) left.eval().map(res2 => res2 / res1)
        else Failure("Division by zero")
      }
    case SquareRoot(expression) =>
      expression.eval().flatMap{result =>
        if(result >= 0)  Success(Math.sqrt(result))
        else Failure("Square root of negative number")
      }
  }
}
final case class Number(value: Double) extends Expression
final case class Addition(left: Expression, right: Expression) extends Expression
final case class Subtraction(left: Expression, right: Expression) extends Expression
final case class Division(left: Expression, right: Expression) extends Expression
final case class SquareRoot(value: Expression) extends Expression

val numberA = Number(20)
val numberB = Number(10)
val expression = Subtraction(Addition(numberA, numberB), Number(5))
expression.eval()
assert(Addition(SquareRoot(Number(-1.0)), Number(2.0)).eval == Failure("Square root of negative number"))
assert(Addition(SquareRoot(Number(4.0)), Number(2.0)).eval == Success(4.0))
assert(Division(Number(4), Number(0)).eval == Failure("Division by zero"))
Addition(SquareRoot(Number(-1.0)), Number(2.0)).eval
Addition(SquareRoot(Number(4.0)), Number(2.0)).eval
Division(Number(4), Number(0)).eval
Division(Number(4), Subtraction(expression, Number(25))).eval

assert(Addition(Number(1), Number(2)).eval == Success(3))
assert(SquareRoot(Number(-1)).eval == Failure("Square root of negative number"))
assert(Division(Number(4), Number(0)).eval == Failure("Division by zero"))
assert(Division(Addition(Subtraction(Number(8), Number(6)), Number(2))
  , Number(2)).eval == Success(2.0))
