package modellingdatawithtraits

object Draw {
  def apply(shape: Shape): String = shape match {
    case Circle(radius, color) => s"A ${colorOfShape(color)}Circle of radius ${radius}cm"
    case Rectangle(width,height, color) => s"A ${colorOfShape(color)}Rectangle of width ${width}cm and height ${height}cm"
    case Square(size, color) => s"A ${colorOfShape(color)}Square of size ${size}cm"
  }

  def colorOfShape(color: Color): String = color match {
    case Red() | Yellow() | Pink() => s"${color.name} "
    case Custom(_, _, _) => ""
  }
}