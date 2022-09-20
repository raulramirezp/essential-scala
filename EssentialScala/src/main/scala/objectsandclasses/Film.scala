package objectsandclasses

class Film(name: String, yearOfRelease: Int, imdbRating: Double, val director: Director){
  def directorsAge() = yearOfRelease - director.yearOfBirth

  def isDirectedBy(director: Director) = director.equals(this.director)

  def copy(
            name: String = this.name,
            yearOfRelease: Int = this.yearOfRelease,
            imdbRating: Double = this.imdbRating,
            director: Director = this.director
          ): Film =
    new Film(name, yearOfRelease, imdbRating, director)

  override def toString: String = s"Film($name,$yearOfRelease,$imdbRating,$director)"
}

class Director(firstName: String, lastName: String, val yearOfBirth: Int){
  def name = s"$firstName $lastName"

  override def toString: String = s"Director($firstName,$lastName,$yearOfBirth)"
}