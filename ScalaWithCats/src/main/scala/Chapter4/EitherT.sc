import cats.data.EitherT
import cats.implicits.catsStdInstancesForFuture

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

def someValidation(string: String): Either[String, Int] = {
  val  number: Try[Int] = Try(string.toInt)
  Either.cond(number.isSuccess, number.get, "Invalid number")
}

def wrapValidation(value: String): EitherT[Future, String, Int] =
  EitherT.fromEither[Future](someValidation(value))

def saveOrderDB(orderID: Int): Future[Int] = Future{
  //val r = scala.util.Random
  //val randomSleepTime = r.nextInt(3000)
  //println(s"Sleep for saveOrderDB is $randomSleepTime")
  Thread.sleep(1400)
  println(s"DB ${Thread.currentThread().getName}")
  orderID*2
}

def saveOrderCache(orderID: Int): Future[Int] = Future{
  // val r = scala.util.Random
  //val randomSleepTime = r.nextInt(1000)
  //println(s"Sleep for saveOrderCache is $randomSleepTime")
  Thread.sleep(800)
  println(s"Cache ${Thread.currentThread().getName}")
  orderID*2
}

val future1 = saveOrderDB(1)
val future2 = saveOrderCache(2)
//Testing Future parallel execution
val result = for {
  first <- future1
  second <- future2
} yield (first , second)

Thread.sleep(1700)
println(s"Result futures is $result")


//Testing EitherT
def priceOrder(orderID: String): EitherT[Future, String, Int] = {
  for{
    number <- wrapValidation(orderID)
    future1 = saveOrderDB(number)
    future2 = saveOrderCache(number+1)
    first <- EitherT.liftF(future1)
    second <- EitherT.liftF(future2)
  } yield number + first + second
}

val resultEitherT1 = priceOrder("100")
Thread.sleep(1700)
println(s"Result eitherT is $resultEitherT1}")