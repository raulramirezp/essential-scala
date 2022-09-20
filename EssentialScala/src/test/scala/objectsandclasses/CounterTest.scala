package objectsandclasses

import org.scalatest._
import flatspec._
import matchers._

class CounterTest extends AnyFlatSpec with should.Matchers {
  "Counter" should "increment and decrement successful" in {
    val counterResult: Int = new Counter(10).inc.dec.inc.inc.count
    counterResult shouldBe 12
  }
}
