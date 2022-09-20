package objectsandclasses

import org.scalatest.flatspec._
import org.scalatest.matchers._

class DirectorTest extends AnyFlatSpec with should.Matchers {
  "Film" should "works successful" in {

    val eastwood = new Director("Clint", "Eastwood", 1930)
    val mcTiernan = new Director("John", "McTiernan", 1951)
    val nolan = new Director("Christopher", "Nolan", 1970)
    val someBody = new Director("Just", "Some Body", 1990)
    val memento = new Film("Memento", 2000, 8.5, nolan)
    val darkKnight = new Film("Dark Knight", 2008, 9.0, nolan)
    val inception = new Film("Inception", 2010, 8.8, nolan)
    val highPlainsDrifter = new Film("High Plains Drifter", 1973, 7.7,
      eastwood)
    val outlawJoseyWales = new Film("The Outlaw Josey Wales", 1976, 7.9,
      eastwood)
    val unforgiven = new Film("Unforgiven", 1992, 8.3, eastwood)
    val granTorino = new Film("Gran Torino", 2008, 8.2, eastwood)
    val invictus = new Film("Invictus", 2009, 7.4, eastwood)
    val predator = new Film("Predator", 1987, 7.9, mcTiernan)
    val dieHard = new Film("Die Hard", 1988, 8.3, mcTiernan)
    val huntForRedOctober = new Film("The Hunt for Red October", 1990,
      7.6, mcTiernan)
    val thomasCrownAffair = new Film("The Thomas Crown Affair", 1999, 6.8,
      mcTiernan)

    eastwood.yearOfBirth shouldBe 1930
    dieHard.director.name shouldBe "John McTiernan"
    invictus.isDirectedBy(nolan) shouldBe false
    invictus.isDirectedBy(eastwood) shouldBe true

    highPlainsDrifter.copy(name = "L'homme des hautes plaines").toString shouldBe "Film(L'homme des hautes plaines,1973,7.7,Director(Clint,Eastwood,1930))"
    val resultTCAFilm = thomasCrownAffair
      .copy(yearOfRelease = 1968, director = new Director("Norman", "Jewison", 1926))
      .toString
    resultTCAFilm shouldBe "Film(The Thomas Crown Affair,1968,6.8,Director(Norman,Jewison,1926))"
    inception.copy().copy().copy().toString shouldBe "Film(Inception,2010,8.8,Director(Christopher,Nolan,1970))"
  }
}
