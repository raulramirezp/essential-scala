
final case class Distribution[A](events: List[(A, Double)]) {
  def normalize: Distribution[A] = {
    val totalWeight = (events map { case (a, p) => p }).sum
    Distribution(events map { case (a, p) => a -> (p / totalWeight) })
  }

  def compact: Distribution[A] = {
    val distinct: List[A] = (events map { case (a, p) => a }).distinct

    def prob(a: A): Double =
      (events filter { case (x, p) => x == a } map { case (a, p) => p }).sum

    Distribution(distinct map { a => a -> prob(a) })
  }

  def map[B](f: A => B): Distribution[B] =
    Distribution(events.map(element => (f(element._1), element._2)))

  def flatMap[B](f: A => Distribution[B]): Distribution[B] =
    Distribution(
      events.flatMap {
        case (value, p) =>
          f(value)
            .events
            .map { case (b, pb) => (b, pb * p) }
      }
    ).compact.normalize

}

object Distribution {
  def uniform[A](list: List[A]): Distribution[A] = {
    val probability = 1.0 / list.size
    Distribution(list.map((_, probability)))
  }
}

val distribution = Distribution.uniform(List(1, 2, 3, 4, 1, 4, 3, 6, 4, 6))

distribution.map(_.toString)
val dist3: Distribution[Distribution[Int]] = Distribution
  .uniform(List(distribution, distribution))

dist3.flatMap(identity)

sealed trait Coin
case object Heads extends Coin
case object Tails extends Coin

val fairCoin: Distribution[Coin] = Distribution.uniform(List(Heads, Tails))

val threeFlips =
  for {
    c1 <- fairCoin
    c2 <- fairCoin
    c3 <- fairCoin
  } yield (c1, c2, c3)