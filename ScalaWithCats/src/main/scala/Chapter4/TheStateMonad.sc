/** allows us to pass additional state around as part of a computation. We
  * define State instances representing atomic state operations and thread them
  * together using map and flatMap. In this way we can model mutable state in a
  * purely functional way, without using actual mutation.
  */
import scala.annotation.tailrec
import scala.util.Try

import cats.Eval
import cats.data.{IndexedStateT, State}
val a = State[Int, String] { state =>
  (state, s"The state is $state")
}
// Get the state and the result:
val (state, result) = a.run(10).value

// Get the state, ignore the result:
val justTheState = a.runS(10).value

// Get the result, ignore the state:
val justTheResult = a.runA(10).value

// Combining
val step1 = State[Int, String] { num =>
  val ans = num + 1
  (ans, s"Result of step 1 $ans")
}

val step2 = State[Int, String] { num =>
  val ans = num * 2
  (ans, s"Result of step 2 $ans")
}

val both: IndexedStateT[Eval, Int, Int, (String, String)] = for {
  ans1 <- step1
  ans2 <- step2
} yield (ans1, ans2)

val (state, result) = both.run(20).value

/** The general model for using the State monad is to represent each step of a
  * computation as an instance and compose the steps using the standard monad
  * operators.
  */
import State._
val program: State[Int, (Int, Int, Int)] = for {
  a <- get[Int] // State[Int, Int](1,1)
  _ <- set[Int](a + 1) // State[Int, Unit](2,())
  b <- get[Int] // State[Int, Int](2,2)
  _ <- modify[Int](_ + 1) // State[Int, Int](3,2)
  c <- inspect[Int, Int](_ * 1000) // State[Int, Int](3,3000)
} yield (a, b, c)

val (state, result) = program.run(1).value

// Exercise: Post‐Order Calculator
// 1 2 + 3 *
trait BinaryOperation

object BinaryOperation {
  def from(symbol: String): (Int, Int) => Int =
    symbol match {
      case "+" => _ + _
      case "-" => _ - _
      case "*" => _ * _
      case "/" => _ / _
    }
}

def operand(number: Int) = State[List[Int], Int] { stack =>
  (number :: stack, number)
}

def operator(fun: (Int, Int) => Int) = State[List[Int], Int] {
  case a :: b :: tail =>
    (
      fun(a, b) :: tail,
      fun(a, b)
    ) // println(s"here a: $a , b: $b, tail: $tail")
}

type CalcState[A] = State[List[Int], A]
def evalOne(sym: String): CalcState[Int] =
  Try(sym.toInt).fold(
    _ => operator(BinaryOperation.from(sym)),
    number => operand(number)
  )

def evalAll(input: List[String]): CalcState[Int] = {
  input.foldLeft(State.empty[List[Int], Int])((acc, value) => {
    acc.flatMap(_ => evalOne(value))
/*    for{
      _ <- acc
      ans <- evalOne(value)
    } yield ans*/
  })

  /*@tailrec
  def evaluate(list: List[String], acc: CalcState[Int]): CalcState[Int] =
    list match {
      case Nil => acc
      case first :: tail =>
        evaluate(
          tail,
          for {
            _ <- acc
            ans <- evalOne(first)
          } yield ans
        )
    }

  evaluate(input, State.empty)*/
}

def evalInput(input: String): Int =
  evalAll(input.split(" ").toList)
    .runA(Nil)
    .value

val program = for {
  _ <- evalOne("1")
  _ <- evalOne("2")
  ans <- evalOne("+")
} yield ans

program.runA(Nil).value

evalAll(List("2", "1", "2", "+", "3", "*", "11", "+", "/"))
  .runA(Nil)
  .value

val biggerProgram = for {
  _ <- evalAll(List("1", "2", "+"))
  _ <- evalAll(List("3", "4", "+"))
  ans <- evalOne("*")
} yield ans
biggerProgram.runA(Nil).value

evalInput("1 2 + 3 4 + *")