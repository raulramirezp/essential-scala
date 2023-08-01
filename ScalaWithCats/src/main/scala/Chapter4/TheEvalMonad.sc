import cats.Eval

import scala.annotation.tailrec

/**
 *
 * • call‐by‐value which is eager and memoized;
 * • call‐by‐name which is lazy and not memoized;
 * • call‐by‐need which is lazy and memoized.
 *
 * The three behaviours are summarized below:
 * Scala         Cats     Properties
 * val           Now     eager, memoized
 * def           Always  lazy, not memoized
 * lazy val      Later   lazy, memoized
 */

println("Scala call by value (eager and memoized)")
val x = {
  println("Computing X")
  math.random
}
println("---------------")
println(x) // first access
println(x) //second access

println("Scala call by name (lazy and not memoized)")
def byName = {
  println("Computing X")
  math.random
}
println("---------------")
println(byName) // first access
println(byName) //second access

println("Scala call by need (lazy and memoized)")

lazy val byNeed = {
  println("Computing X")
  math.random
}
println("---------------")
println(byNeed) // first access
println(byNeed) // second access

// Cats
println("Cats call by value (eager and memoized)")
val evalMonadByValue = Eval.now{
  println("Computing X")
  math.random
}
println("---------------")
println(evalMonadByValue) // first access
println(evalMonadByValue) //second access

println("Cats call by name (lazy and not memoized)")
val evalMonadByName = Eval.always{
  println("Computing X")
  math.random
}
println("---------------")
println(evalMonadByName.value) // first access
println(evalMonadByName.value) //second access

println("Cats call by need (lazy and memoized)")
val evalMonadByNeed = Eval.later{
  println("Computing X")
  math.random
}
println("---------------")
println(evalMonadByNeed.value) // first access
println(evalMonadByNeed.value) //second access

// Map and flapmap over Eval monad
val ans =
  Eval.now{println("Step 1"); 10}.flatMap(
    a => Eval.always{println("Step 2"); (a,15)}
  ).map(tuple => tuple._1 + tuple._2)

println(ans.value) //first access
println(ans.value) //second access

//Memoize method
val saying = Eval
  .always{ println("Step 1"); "The cat" }
  .map{ str => println("Step 2"); s"$str sat on" }
  .memoize
  .map{ str => println("Step 3"); s"$str the mat" }

println(s"First access ${saying.value}")
println(s"Second access ${saying.value}")

/**
 * Trampolining and Eval.defer
 * One useful property of Eval is that its map and flatMap methods are trampolined.
 * This means we can nest calls to map and flatMap arbitrarily without consuming stack frames.
 * We call this property “stack safety”.
 */

/*
java.lang.StackOverflowError
def factorial(n: BigInt): BigInt =
  if(n == 1) n else n * factorial(n - 1)

factorial(50000)*/

@tailrec
def factorialTailRec(n: BigInt, acc: BigInt): BigInt =
  if(n==1) acc
  else factorialTailRec(n-1, acc * n)

factorialTailRec(50000,1)

def factorialWithEvalMonad(n: BigInt): Eval[BigInt] =
  if (n==1) Eval.now(n)
  else Eval.defer(factorialWithEvalMonad(n-1).map(_ * n))

factorialWithEvalMonad(50000).value

/**
 * There are still limits on how deeply we can nest computations,
but they are bounded by the size of the heap rather than the stack
 */

// Exercise: Safer Folding using Eval

def foldRight[A, B](as: List[A], acc: B)(fn: (A, B) => B): B = as match {
  case head :: tail =>
    fn(head, foldRight(tail, acc)(fn))
  case Nil =>
    acc
}
def foldRightEval[A, B](as: List[A], acc: B)(fn: (A, B) => B): Eval[B] = as match {
  case head :: tail =>
    Eval.defer(foldRightEval(tail, acc)(fn).map(fn(head, _)))
  case Nil =>
    Eval.now(acc)
}

foldRightEval((1 to 100000).toList, 0L)(_ + _).value
