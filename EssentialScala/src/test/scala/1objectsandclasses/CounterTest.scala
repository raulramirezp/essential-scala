package objectsandclasses

import org.scalatest._
import flatspec._
import matchers._

class CounterTest extends AnyFlatSpec with should.Matchers {
  "Counter" should "increment and decrement successful" in {
    val counterResult: Int = new Counter(10).inc().dec.inc.inc.count
    counterResult shouldBe 12
  }

  it should "added a value successful" in {
    val adder = new Adder(10)
    val result = new Counter(5).adjust(adder)
    result.count shouldBe 15
    result.methodsAreFunctions(adder.add) shouldBe 25
  }
}
