package objectsandclasses

case class FilmCS(name: String, yearOfRelease: Int, imdbRating: Double, director: DirectorCS){
  def directorsAge() = yearOfRelease - director.yearOfBirth

  def isDirectedBy(director: DirectorCS) = director.equals(this.director)
}

object FilmCS{
  def highestRating(filmOne: FilmCS, filmTwo: FilmCS): FilmCS =
    if (filmOne.imdbRating > filmTwo.imdbRating)
      filmOne
    else
      filmTwo

  def oldestDirectorCSAtTheTime(filmOne: FilmCS, filmTwo: FilmCS): DirectorCS =
    if (filmOne.directorsAge() > filmTwo.directorsAge())
      filmOne.director
    else
      filmTwo.director
}
