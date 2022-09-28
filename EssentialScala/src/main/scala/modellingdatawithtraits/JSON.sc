sealed trait Sequence[+E]{
  def length: Int = this match {
    case Nil => 0
    case Pair(_, tail) => 1 + tail.length
  }

  def foreach(fn: E => Unit): Unit = this match {
    case Nil => ()
    case Pair(head, tail) =>
      fn(head)
      tail.foreach(fn)
  }
}

case object Nil extends Sequence[Nothing]
final case class Pair[+E](head: E, tail: Sequence[E]) extends Sequence[E]

sealed trait JSON
sealed trait JsonData
final case class SingleValue(data: Any) extends JsonData
final case class JsonValue(data: JSON) extends JsonData
final case class JsonSeq(data: Sequence[JsonData]) extends JsonData


final case class JsonMap(name: String, value: JsonData) extends JSON
final case class JsonObject(content: Sequence[JSON]) extends JSON


def printJson(json: JSON): Unit = {
  def printJsonData(jsonData: JsonData): Unit = jsonData match {
    case SingleValue(data) => print(s"$data, ")
    case JsonValue(data) => printJson(data)
    case JsonSeq(data) =>
      print("[")
      data.foreach(printJsonData)
      print("]")
  }
  json match {
    case JsonObject(content) =>
      print("{")
      content.foreach(printJson)
      print("}")
    case JsonMap(name, value) =>
      print(s"\"$name\": ")
      printJsonData(value)
      print(",")
  }
}

val seq: Sequence[JsonData] = Pair(SingleValue("\"a string\""), Pair(SingleValue(1.0), Pair(SingleValue(true), Nil)))
val json1 = JsonObject(Pair[JSON](
  JsonMap("a", JsonSeq(seq)),
  Nil))
printJson(json1)

val seqNumbers = JsonSeq(Pair(SingleValue(1),Pair(SingleValue(2),Pair(SingleValue(3),Nil))))
val seqChar = JsonSeq(Pair(SingleValue("\"a\""),Pair(SingleValue("\"b\""),Pair(SingleValue("\"c\""),Nil))))
val pair1 = JsonMap("a",seqNumbers)
val pair2 = JsonMap("b", seqChar)
val pair12 = JsonMap("doh", SingleValue(true))
val pair22 = JsonMap("ray", SingleValue(false))
val pair33 = JsonMap("me", SingleValue(1))
val nested = JsonValue(JsonObject(Pair(pair12, Pair(pair22, Pair(pair33, Nil)))))
val nestedJson = JsonMap("c",nested)
val json2 = JsonObject(Pair(pair1, Pair(pair2, Pair(nestedJson, Nil))))
printJson(json2)