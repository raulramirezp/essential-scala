package Chapter6.code
//import cats.implicits.{toFlatMapOps, toFunctorOps}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object SemigroupalTests extends App{

/*  def product[F[_] : Monad, A, B](fa: F[A], fb: F[B]): F[(A, B)] =
    fa.flatMap(a =>
      fb.map(b =>
        (a, b)
      )
    )*/

  //En el momento en el que la función recibe el futuro como parametro se inicializa la ejecución, por eso se ejecutan
  // en paralelo
  def productForFuture[A, B](fa: Future[A], fb: Future[B]): Future[(A, B)] =
    fa.flatMap( a => fb.map(b => (a,b)))

  def mapTuple[A,B](future: Future[(A, B)])(fn: (A, B) => B): Future[B] =
    future.map[B](tuple => fn(tuple._1, tuple._2))
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
      Thread.sleep(600)
      println(s"the age is ${age.toString}")
      age
    }

  //val futureProduct: Future[(String, Int)] = product(getName(), getAge())
  //val res = Functor[Future].map(product(getName(), getAge())) { tuple =>
  /*val res = Functor[Future].map(productForFuture(getName(), getAge())) { tuple =>
    println(s"Cat(${tuple._1}, ${tuple._2}) from res2")
    tuple._2
  }*/

  val res = mapTuple(productForFuture(getName(), getAge()))( { (a,b) =>
    println(s"Cat(${a}, ${b}) from res2")
    b
  })

  Await.result(res, 2.second)

}
