package Chapter6.code

import cats.data.EitherT
import cats.implicits.{catsSyntaxEitherId, catsSyntaxTuple2Semigroupal}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object TestTupleMappingFuturesInsideEitherT extends App {

  /** Here, the futures are executed in parallel because: When you call
    * getCatName() or getCatAge(), you are constructing an EitherT with a
    * Future. This means that as soon as you call these methods, the futures are
    * scheduled for execution, and they start running concurrently.
    *
    * Even though the computations inside the futures are defined within methods
    * and might be lazily evaluated, the scheduling of the futures themselves
    * occurs when the methods are invoked
    */
  def getCatName(): EitherT[Future, Int, String] = EitherT {
    Future {
      val name = "Patikalvo"
      Thread.sleep(600)
      println(s"the name is $name")
      name.asRight[Int]
    }
  }

  def getCatAge(): EitherT[Future, Int, Int] = EitherT {
    Future {
      val age = 3
      Thread.sleep(600)
      println(s"the age is ${age.toString}")
      age.asRight[Int]
    }
  }

  lazy val res: EitherT[Future, Int, Int] = (getCatName(), getCatAge()).mapN {
    (name, age) =>
      println(s"Cat($name, $age)")
      age
  }
  Await.result(res.value, 2.second)

  def getName(): Future[String] =
    Future {
      val name = "Patikalvo"
      Thread.sleep(600)
      println(s"the name is $name")
      name
    }

  def getAge(): Future[Int] =
    Future {
      val age = 4
      Thread.sleep(300)
      println(s"the age is ${age.toString}")
      age
    }

  val res2 = (getName(), getAge()).mapN { (name, age) =>
    println(s"Cat($name, $age) from res2")
    age
  }
  Await.result(res2, 2.second)

  /** To be able to execute Futures in parallel inside a for-yield expression we
    * must declare the Futures using val because the Futures will be scheuled
    * for execution, if we define the futures inside def using for-yield the
    * Futures are executed sequentially
    */
 /* val name = getName()

  val age = getAge()

  val res3 = for {
    name <- name
    age <- age
  } yield {
    println(s"Cat($name, $age)")
    age
  }

  Await.result(res3, 2.second)*/

 val f = getName()
  val g = getAge()
  val res4 = f flatMap{ (name: String) => g map { age => age } }
  Await.result(res4, 2.second)
}
