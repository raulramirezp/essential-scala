/**
 * What’s in a Monad?
 * Broadly speaking, a monad is a generic type that allows us to sequence computations
 * while abstracting away some technicality
 *
 * We do the sequencing using for comprehensions, worrying only about the programming logic we care about.
 * The code hidden in the monad’s map and flatMap methods does all of the plumbing for us. For example:
 * * Option is a monad
 * * Seq is a monad
 * * Future is another popular monad
 *
 * 6.6.2 Exercises
 */
import scala.util.Try
val opt1 = Some(1)
val opt2 = Some(2)
val opt3 = Some(3)
val seq1 = Seq(1)
val seq2 = Seq(2)
val seq3 = Seq(3)
val try1 = Try(1)
val try2 = Try(2)
val try3 = Try(3)

for {
  a <- opt1
  b <- opt2
  c <- opt3
} yield  a + b + c

for {
  a <- seq1
  b <- seq2
  c <- seq3
} yield  a + b + c

for {
  a <- try1
  b <- try2
  c <- try3
} yield  a + b + c

/**
 * FOR COMPREHENSIONS REDUX
 */
// filtering
for(x <- Seq(-2, -1, 0, 1, 2) if x > 0) yield x
// Parallel Iteration: zip
for(x <- Seq(1, 2, 3).zip(Seq(4, 5, 6))) yield { val (a, b) = x; a + b }

Seq(1, 2, 3).zipWithIndex

"a" -> 1
// 6.8.3 Exercises