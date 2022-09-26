package objectsandclasses

import org.scalatest.flatspec._
import org.scalatest.matchers._

class DirectorCSTest extends AnyFlatSpec with should.Matchers {
  "Film" should "works successful" in {

    val eastwood = DirectorCS("Clint", "Eastwood", 1930)
    val mcTiernan = DirectorCS("John", "McTiernan", 1951)
    val nolan = DirectorCS("Christopher", "Nolan", 1970)
    val someBody = DirectorCS("Just", "Some Body", 1990)
    val memento = FilmCS("Memento", 2000, 8.5, nolan)
    val darkKnight = FilmCS("Dark Knight", 2008, 9.0, nolan)
    val inception = FilmCS("Inception", 2010, 8.8, nolan)
    val highPlainsDrifter = FilmCS("High Plains Drifter", 1973, 7.7,
      eastwood)
    val outlawJoseyWales = FilmCS("The Outlaw Josey Wales", 1976, 7.9,
      eastwood)
    val unforgiven = FilmCS("Unforgiven", 1992, 8.3, eastwood)
    val granTorino = FilmCS("Gran Torino", 2008, 8.2, eastwood)
    val invictus = FilmCS("Invictus", 2009, 7.4, eastwood)
    val predator = FilmCS("Predator", 1987, 7.9, mcTiernan)
    val dieHard = FilmCS("Die Hard", 1988, 8.3, mcTiernan)
    val huntForRedOctober = FilmCS("The Hunt for Red October", 1990,
      7.6, mcTiernan)
    val thomasCrownAffair = FilmCS("The Thomas Crown Affair", 1999, 6.8,
      mcTiernan)

    eastwood.yearOfBirth shouldBe 1930
    dieHard.director.name shouldBe "John McTiernan"
    invictus.isDirectedBy(nolan) shouldBe false
    invictus.isDirectedBy(eastwood) shouldBe true

    highPlainsDrifter.copy(name = "L'homme des hautes plaines").toString shouldBe "FilmCS(L'homme des hautes plaines,1973,7.7,DirectorCS(Clint,Eastwood,1930))"
    val resultTCAFilmCS = thomasCrownAffair
      .copy(yearOfRelease = 1968, director = DirectorCS("Norman", "Jewison", 1926))
      .toString
    resultTCAFilmCS shouldBe "FilmCS(The Thomas Crown Affair,1968,6.8,DirectorCS(Norman,Jewison,1926))"
    inception.copy().copy().copy().toString shouldBe "FilmCS(Inception,2010,8.8,DirectorCS(Christopher,Nolan,1970))"
  }

  it should "return the highest rating FilmCS" in {
    val mcTiernan = DirectorCS("John", "McTiernan", 1951)
    val predator = FilmCS("Predator", 1987, 7.9, mcTiernan)
    val dieHard = FilmCS("Die Hard", 1988, 8.3, mcTiernan)

    FilmCS.highestRating(predator, dieHard) shouldBe dieHard
  }

  it should "return the oldest director at time of filming" in {
    val eastwood = DirectorCS("Clint", "Eastwood", 1930)
    val mcTiernan = DirectorCS("John", "McTiernan", 1951)
    val predator = FilmCS("Predator", 1987, 7.9, mcTiernan)
    val unforgiven = FilmCS("Unforgiven", 1992, 8.3, eastwood)

    FilmCS.oldestDirectorCSAtTheTime(predator, unforgiven) shouldBe eastwood
  }

  "DirectorCS" should "return the oldest director" in {
    val someBody = DirectorCS("Just", "Some Body", 1990)
    val mcTiernan = DirectorCS("John", "McTiernan", 1951)

    DirectorCS.older(someBody, mcTiernan) shouldBe mcTiernan
  }
}