package objectsandclasses

class Director(firstName: String, lastName: String, val yearOfBirth: Int) {
  def name = s"$firstName $lastName"

  override def toString: String = s"Director($firstName,$lastName,$yearOfBirth)"
}

object Director {
  def apply(firstName: String, lastName: String, yearOfBirth: Int) =
    new Director(firstName, lastName, yearOfBirth)

  def older(directorOne: Director, directorTwo: Director): Director =
    if (directorOne.yearOfBirth > directorTwo.yearOfBirth)
      directorTwo
    else
      directorOne
}
