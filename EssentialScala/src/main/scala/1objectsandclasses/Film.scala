package objectsandclasses

class Film(
    val name: String,
    val yearOfRelease: Int,
    val imdbRating: Double,
    val director: Director
) {
  def directorsAge() = yearOfRelease - director.yearOfBirth

  def isDirectedBy(director: Director) = director.equals(this.director)

  def copy(
      name: String = this.name,
      yearOfRelease: Int = this.yearOfRelease,
      imdbRating: Double = this.imdbRating,
      director: Director = this.director
  ): Film =
    new Film(name, yearOfRelease, imdbRating, director)

  override def toString: String =
    s"Film($name,$yearOfRelease,$imdbRating,$director)"
}

object Film {
  def apply(
      name: String,
      yearOfRelease: Int,
      imdbRating: Double,
      director: Director
  ) =
    new Film(name, yearOfRelease, imdbRating, director)

  def highestRating(filmOne: Film, filmTwo: Film): Film =
    if (filmOne.imdbRating > filmTwo.imdbRating)
      filmOne
    else
      filmTwo

  def oldestDirectorAtTheTime(filmOne: Film, filmTwo: Film): Director =
    if (filmOne.directorsAge() > filmTwo.directorsAge())
      filmOne.director
    else
      filmTwo.director
}
