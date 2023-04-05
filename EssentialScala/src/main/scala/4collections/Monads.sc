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
} yield a + b + c

for {
  a <- seq1
  b <- seq2
  c <- seq3
} yield a + b + c

for {
  a <- try1
  b <- try2
  c <- try3
} yield a + b + c

/**
 * FOR COMPREHENSIONS REDUX
 */
// filtering
for (x <- Seq(-2, -1, 0, 1, 2) if x > 0) yield x
// Parallel Iteration: zip
for (x <- Seq(1, 2, 3).zip(Seq(4, 5, 6))) yield {
  val (a, b) = x; a + b
}

Seq(1, 2, 3).zipWithIndex

"a" -> 1
// 6.8.3 Exercises

val people = Set(
  "Alice",
  "Bob",
  "Charlie",
  "Derek",
  "Edith",
  "Fred")
val ages = Map(
  "Alice" -> 20,
  "Bob" -> 30,
  "Charlie" -> 50,
  "Derek" -> 40,
  "Edith" -> 10,
  "Fred" -> 60)
val favoriteColors = Map(
  "Bob" -> "green",
  "Derek" -> "magenta",
  "Fred" -> "yellow")
val favoriteLolcats = Map(
  "Alice" -> "Long Cat",
  "Charlie" -> "Ceiling Cat",
  "Edith" -> "Cloud Cat")

def favoriteColor(personName: String): String =
  favoriteColors.get(personName) match {
    case Some(value) => value
    case None => "Beige"
  }

favoriteColor("Bob")
favoriteColor("Lina")

def printColors(): Unit = favoriteColors.foreach(pair => println(pair._2))

def lookup(name: String, map: Map[String, Any]): Option[Any] =
  map.get(name)

lookup("Fred", ages)

favoriteColor(ages.toList.maxBy(_._2)._1)

def union[A](setA: Set[A], setB: Set[A]): Set[A] = {
  setA.foldLeft(setB)((set,a) => set + a)
  }

union(Set(1,2,3), Set(4,5,6))

// Union of Maps
def unionMap[A](map1: Map[A, Int], map2: Map[A, Int]): Map[A, Int] =
  map1.map(pair => (pair._1, map2.getOrElse(pair._1, 0) + pair._2))

unionMap(Map('a' -> 1, 'b' -> 2), Map('a' -> 2, 'b' -> 4))

def genericUnion[M[_] <: Iterable[_]](m1: M[_], m2: M[_]): Iterable[_] = {
  m1 ++ m2
}

genericUnion(Set(1,2,3), Set(4,5,6))
genericUnion(Map('a' -> 1, 'b' -> 2), Map('a' -> 2, 'b' -> 4))