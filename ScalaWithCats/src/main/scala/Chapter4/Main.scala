package Chapter4

import cats.{Applicative, NonEmptyParallel, Parallel}
import cats.data.EitherT
import cats.implicits._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

object Main extends App {

  def someValidation(string: String): Either[String, Int] = {
    val number: Try[Int] = Try(string.toInt)
    Either.cond(number.isSuccess, number.get, "Invalid number")
  }

  def wrapValidation(value: String): EitherT[Future, String, Int] =
    EitherT.fromEither[Future](someValidation(value))

  def saveOrderDB(orderID: Int, fromMethod: String): Future[Int] = Future {
    val r = scala.util.Random
    val randomSleepTime = r.nextInt(3000)
    println(s"Sleep for saveOrderDB is $randomSleepTime from: $fromMethod")
    Thread.sleep(randomSleepTime)
    println(s"DB thread ${Thread.currentThread().getName} from: $fromMethod")
    orderID * 2
  }

  def saveOrderCache(orderID: Int, fromMethod: String): Future[Int] = Future {
     val r = scala.util.Random
    val randomSleepTime = r.nextInt(1000)
    println(s"Sleep for saveOrderCache is $randomSleepTime from: $fromMethod")
    Thread.sleep(randomSleepTime)
    println(s"Cache thread ${Thread.currentThread().getName} from: $fromMethod")
    orderID * 2
  }

  def exampleUsingParallelFutures(): Unit = {
    println("------------- parallel futures -------------")
    val future1 = saveOrderDB(1, "exampleUsingParallelFutures")
    val future2 = saveOrderCache(2, "exampleUsingParallelFutures")
    //Testing Future parallel execution
    val result = for {
      first <- future1
      second <- future2
    } yield (first, second)


    Thread.sleep(3000)
    println(s"Result futures is $result")
  }

  def exampleUsingEitherTCreatingFutures(): Unit = {
    println("------------- Testing EitherT using val --------------")

    def priceOrder(orderID: String): EitherT[Future, String, Int] = {
      for {
        number <- wrapValidation(orderID)
        future1 = saveOrderDB(number, "exampleUsingEitherTCreatingFutures")
        future2 = saveOrderCache(number + 1, "exampleUsingEitherTCreatingFutures")
        first <- EitherT.liftF(future1)
        second <- EitherT.liftF(future2)
      } yield number + first + second
    }

    val resultEitherT1 = priceOrder("100")
    Thread.sleep(3000)
    println(s"Result eitherT is $resultEitherT1}")
  }

  def exampleUsingEitherTCallingMethods(): Unit = {
    //Testing EitherT using def
    println("-------------  Testing EitherT using def ------------- ")

    def eitherTUsingDef(orderID: String): EitherT[Future, String, Int] = {
      for {
        number <- wrapValidation(orderID)
        first <- EitherT.liftF(saveOrderDB(number, "exampleUsingEitherTCallingMethods"))
        second <- EitherT.liftF(saveOrderCache(number + 1, "exampleUsingEitherTCallingMethods"))
      } yield number + first + second
    }

    val resultEitherTUsingDef = eitherTUsingDef("100")
    Thread.sleep(3000)
    println(s"Result eitherTUsingDef is $resultEitherTUsingDef}")
  }

  def exampleUsingApplicativeMap(): Unit = {
    println("-------------  Testing EitherT using applicative map ------------- ")

    type MyType[T] = EitherT[Future, String, T]

    def test(orderID: String): MyType[Int] = {
      for {
        number <- wrapValidation(orderID)
        result <- Applicative[MyType].map2(
          EitherT.right[String](saveOrderDB(number, "exampleUsingApplicativeMap")),
          EitherT.right[String](saveOrderCache(number + 1, "exampleUsingApplicativeMap"))
        )(_ + _)
      } yield result
    }

    val result = test("100")
    Thread.sleep(3000)
    println(s"Result eitherTUsingDef is $result}")
  }

  def exampleUsingParMapN(): Unit = {
    println("-------------  Testing EitherT using Parallel parMapN ------------- ")

    type MyType[T] = EitherT[Future, String, T]

    def test(orderID: String): MyType[Int] = {
      for {
        number <- wrapValidation(orderID)
        r <- (
          EitherT.right[String](saveOrderDB(number, "exampleUsingParMapN")),
          EitherT.right[String](saveOrderCache(number + 1, "exampleUsingParMapN"))
        ).parMapN(_ + _)
      } yield r
    }

    val result = test("100")
    Thread.sleep(3000)
    println(s"Result eitherTUsingDef is $result}")
  }

  exampleUsingParallelFutures()
  exampleUsingEitherTCreatingFutures()
  exampleUsingEitherTCallingMethods()
  exampleUsingApplicativeMap()
  exampleUsingParMapN()
}
