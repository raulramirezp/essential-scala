import objectsandclasses.{DirectorCS, FilmCS}

/*
Pattern matching is like an extended if expression that allows us to evaluate
an expression depending on the “shape” of the data
 */

case class Cat(colour: String, food: String)


object ChipShop{
  def willServe(cat: Cat): Boolean = cat match {
    case Cat(_, "chips") => true
    case _ => false
  }
}

ChipShop.willServe(Cat("Kosianfiro", "monello"))
ChipShop.willServe(Cat("Alaska", "chips"))


// Get Off My Lawn
object Dad{
  def rate(film: FilmCS): Double = film match {
    case FilmCS(_, _, _, DirectorCS("Clint","Eastwood", _)) => 10.0
    case FilmCS(_, _, _, DirectorCS("John","McTiernan", _)) => 7.0
    case _ => 3.0
  }
}

val eastwood = DirectorCS("Clint", "Eastwood", 1930)
val mcTiernan = DirectorCS("John", "McTiernan", 1951)
val nolan = DirectorCS("Christopher", "Nolan", 1970)

val unforgiven = FilmCS("Unforgiven", 1992, 8.3, eastwood)
val granTorino = FilmCS("Gran Torino", 2008, 8.2, eastwood)
val invictus = FilmCS("Invictus", 2009, 7.4, eastwood)
val predator = FilmCS("Predator", 1987, 7.9, mcTiernan)
val darkKnight = FilmCS("Dark Knight", 2008, 9.0, nolan)

Dad.rate(unforgiven)
Dad.rate(granTorino)
Dad.rate(invictus)
Dad.rate(predator)
Dad.rate(darkKnight)

