package objectsandclasses

import org.scalatest.flatspec._
import org.scalatest.matchers._

class DirectorTest extends AnyFlatSpec with should.Matchers {
  "Film" should "works successful" in {

    val eastwood = Director("Clint", "Eastwood", 1930)
    val mcTiernan = Director("John", "McTiernan", 1951)
    val nolan = Director("Christopher", "Nolan", 1970)
    val someBody = Director("Just", "Some Body", 1990)
    val memento = Film("Memento", 2000, 8.5, nolan)
    val darkKnight = Film("Dark Knight", 2008, 9.0, nolan)
    val inception = Film("Inception", 2010, 8.8, nolan)
    val highPlainsDrifter = Film("High Plains Drifter", 1973, 7.7,
      eastwood)
    val outlawJoseyWales = Film("The Outlaw Josey Wales", 1976, 7.9,
      eastwood)
    val unforgiven = Film("Unforgiven", 1992, 8.3, eastwood)
    val granTorino = Film("Gran Torino", 2008, 8.2, eastwood)
    val invictus = Film("Invictus", 2009, 7.4, eastwood)
    val predator = Film("Predator", 1987, 7.9, mcTiernan)
    val dieHard = Film("Die Hard", 1988, 8.3, mcTiernan)
    val huntForRedOctober = Film("The Hunt for Red October", 1990,
      7.6, mcTiernan)
    val thomasCrownAffair = Film("The Thomas Crown Affair", 1999, 6.8,
      mcTiernan)

    eastwood.yearOfBirth shouldBe 1930
    dieHard.director.name shouldBe "John McTiernan"
    invictus.isDirectedBy(nolan) shouldBe false
    invictus.isDirectedBy(eastwood) shouldBe true

    highPlainsDrifter.copy(name = "L'homme des hautes plaines").toString shouldBe "Film(L'homme des hautes plaines,1973,7.7,Director(Clint,Eastwood,1930))"
    val resultTCAFilm = thomasCrownAffair
      .copy(yearOfRelease = 1968, director = Director("Norman", "Jewison", 1926))
      .toString
    resultTCAFilm shouldBe "Film(The Thomas Crown Affair,1968,6.8,Director(Norman,Jewison,1926))"
    inception.copy().copy().copy().toString shouldBe "Film(Inception,2010,8.8,Director(Christopher,Nolan,1970))"
  }

  it should "return the highest rating film" in {
    val mcTiernan = Director("John", "McTiernan", 1951)
    val predator = Film("Predator", 1987, 7.9, mcTiernan)
    val dieHard = Film("Die Hard", 1988, 8.3, mcTiernan)

    Film.highestRating(predator, dieHard) shouldBe dieHard
  }

  it should "return the oldest director at time of filming" in {
    val eastwood = Director("Clint", "Eastwood", 1930)
    val mcTiernan = Director("John", "McTiernan", 1951)
    val predator = Film("Predator", 1987, 7.9, mcTiernan)
    val unforgiven = Film("Unforgiven", 1992, 8.3, eastwood)

    Film.oldestDirectorAtTheTime(predator, unforgiven) shouldBe eastwood
  }

  "Director" should "return the oldest director" in {
    val someBody = Director("Just", "Some Body", 1990)
    val mcTiernan = Director("John", "McTiernan", 1951)

    Director.older(someBody, mcTiernan) shouldBe mcTiernan
  }

}
