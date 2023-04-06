package objectsandclasses

class Counter(val count: Int) {
  def inc() = new Counter(count + 1)
  def dec() = new Counter(count - 1)

  def methodsAreFunctions(fn: Int => Int): Int =
    fn(count)

  def adjust(adder: Adder) =
    new Counter(adder.add(count))
}

class Adder(amount: Int) {
  def add(in: Int) = in + amount
}
