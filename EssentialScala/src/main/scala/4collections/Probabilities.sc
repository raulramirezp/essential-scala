
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

  def discrete[A](events: List[(A, Double)]): Distribution[A] =
    Distribution(events).compact.normalize
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

sealed trait Food

case object ReadyToEatWithSmell extends Food

case object RawWithoutSmell extends Food

sealed trait Cat

case object CatComesToHarass extends Cat

case object CatStaysSleep extends Cat

val foodEvents: Distribution[Food] = Distribution.discrete(List(ReadyToEatWithSmell -> 0.3, RawWithoutSmell -> 0.7))
val catEventsIfFoodSmell: Distribution[Cat] = Distribution.discrete(List(CatComesToHarass -> 0.8, CatStaysSleep -> 0.2))
val catEventsIfFoodNotSmell: Distribution[Cat] = Distribution.discrete(List(CatComesToHarass -> 0.4, CatStaysSleep -> 0.6))

foodEvents.flatMap {
  case f@ReadyToEatWithSmell => catEventsIfFoodSmell.map(ff => (f, ff))
  case f@RawWithoutSmell => catEventsIfFoodNotSmell.map(ff => (f, ff))
}

val model: Distribution[(Food, Cat)] = for {
  p1 <- foodEvents
  p2 <- p1 match {
    case ReadyToEatWithSmell => catEventsIfFoodSmell
    case RawWithoutSmell => catEventsIfFoodNotSmell
  }
} yield (p1,p2)

val generalProbCatComesToHarass = model.events.filter{
  case ((_, cat),_) if cat == CatComesToHarass => true
  case _ => false
}.map{case (_, p) => p}.sum

val probCatHarassWhenFoodSmell =
  model.events.find {
    case ((food, cat), _) if food == ReadyToEatWithSmell && cat == CatComesToHarass => true
  }.map{case (pair, probability) => probability/generalProbCatComesToHarass}

/*
sealed trait Food

case object Raw extends Food

case object Cooked extends Food

val food: Distribution[Food] = Distribution.discrete(List(Cooked -> 0.3, Raw -> 0.7))

sealed trait Cat

case object Asleep extends Cat

case object Harassing extends Cat

def cat(food: Food): Distribution[Cat] =
  food match {
    case Cooked => Distribution.discrete(List(Harassing -> 0.8, Asleep -> 0.2))
    case Raw => Distribution.discrete(List(Harassing -> 0.4, Asleep -> 0.6))
  }

val foodModel: Distribution[(Food, Cat)] =
  for {
    f <- food
    c <- cat(f)
  } yield (f, c)


// Probability the cat is harassing me
val pHarassing: Double =
  foodModel.events.filter {
    case ((_, Harassing), _) => true
    case ((_, Asleep), _) => false
  }.map { case (a, p) => p }.sum
// Probability the food is cooked given the cat is harassing me
val pCookedGivenHarassing: Option[Double] =
  foodModel.events collectFirst[Double] {
    case ((Cooked, Harassing), p) => p
  } map (_ / pHarassing)*/
