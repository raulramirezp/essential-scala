package modellingdatawithtraits

sealed trait Color {
  def red: Int
  def green: Int
  def blue: Int
  def name: String
  def tone: String = (red, green, blue) match {
    case (0, _, _) | (_, 0, _) | (_, _, 0) => "Dark"
    case (red, green, blue) if red <= 128 && green <= 128 && blue <= 128 =>
      "Dark"
    case _ => "light"
  }
}

final case class Custom(red: Int, green: Int, blue: Int) extends Color {
  val name: String = "custom"
}

case object Red extends Color {
  val red: Int = 255
  val green: Int = 0
  val blue: Int = 0
  val name: String = "Red"
}

case object Yellow extends Color {
  val red: Int = 255
  val green: Int = 255
  val blue: Int = 0
  val name: String = "Yellow"
}

case object Pink extends Color {
  val red: Int = 255
  val green: Int = 0
  val blue: Int = 255
  val name: String = "Pink"
}
