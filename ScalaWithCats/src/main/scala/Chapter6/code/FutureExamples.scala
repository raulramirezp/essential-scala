package Chapter6.code

import cats.implicits.catsSyntaxTuple2Semigroupal
/*import monix.eval.Task
import monix.execution.Scheduler*/

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object FutureExamples extends App {
  def saveIfNotExists(value: String, time: Long): Future[Boolean] =
    Future {
      Thread.sleep(time)
      println(s"Saving $value after $time ms")
      true
    }

  println("--------- Future in for-yield using def: Sequentially ---------")
  val futureRes = for {
    a <- saveIfNotExists("test1", 3000)
    b <- saveIfNotExists("test2", 1000)
  } yield a && b

  val result = Await.result(futureRes, 4.seconds)
  println(s"The result is $result")

  println("--------- Future in for-yield: Sequentially ---------")
  val futureRes2 = for {
    a <- Future {
      Thread.sleep(3000)
      println(s"Hi from future 1")
      true
    }
    b <- Future {
      Thread.sleep(1000)
      println(s"Hi from future 2")
      true
    }
  } yield a && b

  val result2 = Await.result(futureRes2, 5.seconds)
  println(s"The result is $result2")

  println("--------- Future in for-yield inside def: Parallel ---------")

  //En el momento en el que la función recibe el futuro como parametro se inicializa la ejecución, por eso se ejecutan
  // en paralelo
  def executeFutures(fa: Future[Boolean], fb: Future[Boolean]): Future[Boolean] =
    fa flatMap(a => fb map (b => !(a && b)))
    /*for {
      a <- fa
      b <- fb
    } yield !(a && b)*/

  val resultFuture =
    Await.result(executeFutures(saveIfNotExists("test1", 3000), saveIfNotExists("test2", 1000)), 4.seconds)
  println(s"The result is $resultFuture")

  println("--------- Future with mapN: Parallel ---------")
  val resultUsingTuple = Await.result(
    (
      saveIfNotExists("test1", 3000),
      saveIfNotExists("test2", 1000)
    ).mapN(_ && _),
    4.seconds
  )

  println(s"The result is $resultUsingTuple")
 /* println("--------- Future with mapN: Parallel ---------")
  val resultTaskMapN = Await.result(
    Task
      .deferFuture(
        (
          saveIfNotExists("test1", 3000),
          saveIfNotExists("test2", 1000)
        ).mapN(_ && _)
      )
      .runToFuture(Scheduler.global),
    4.seconds
  )

  println("--------- parMapN ---------: Parallel")
  val resultParMap: Task[Boolean] = (
    Task.deferFuture(saveIfNotExists("First Task", 3000)),
    Task.deferFuture(saveIfNotExists("Second Task", 1000))
  ).parMapN((saved, savedV2) => saved && savedV2)

  resultParMap.runToFuture(Scheduler.global)*/
  Thread.sleep(4000)
}
