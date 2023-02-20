package modellingdatawithtraits

sealed trait DivisionResult

final case class Finite(value: Int) extends DivisionResult

case object Infinite extends DivisionResult

object divide{
  def apply(a: Int, b: Int): DivisionResult = (a, b) match {
    case (_, 0)  => Infinite
    case (_,_)  => Finite(a/b)
  }
}