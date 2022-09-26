package objectsandclasses

case class DirectorCS(firstName: String, lastName: String, yearOfBirth: Int){
  def name = s"$firstName $lastName"
}
object DirectorCS{
  def older(directorOne: DirectorCS, directorTwo: DirectorCS): DirectorCS =
    if (directorOne.yearOfBirth > directorTwo.yearOfBirth)
      directorTwo
    else
      directorOne
}