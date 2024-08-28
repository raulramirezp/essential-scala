import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * If we impose some conditions, we can tame the side effects into something safer;
 * we’ll call these effects. There are two parts:
1. The type of the program should tell us what kind of effects the program will perform,
   in addition to the type of the value it will produce.
2. If the behavior we want relies upon some externally-visible side effect,
   we separate describing the effects we want to happen from actually making them happen.
 */

val print = Future(println("Hello World!"))
val twice = print.flatMap(_ => print)

case class MyIO[A](runUnsafe: () => A){
  def map[B](f: A => B): MyIO[B] = {
    MyIO[B](() => f(runUnsafe()))
  }

  def flatMap[B](f: A => MyIO[B]): MyIO[B] = {
    MyIO(() => f(runUnsafe()).runUnsafe())
  }
}

object MyIO{
  def putString(str: => String): MyIO[Unit] = {
    MyIO(() => println(str))
  }
}

val patikalvo = MyIO.putString("Patikalvo")
patikalvo.map(_ => println("Done"))
MyIO
  .putString("Hola Monada IO")
  .runUnsafe()

patikalvo
  .map(_ => println("Done"))
  .runUnsafe()

val hello = MyIO.putString("hello!")
val world = MyIO.putString("world!")

hello.flatMap(_ => world).runUnsafe()

val helloWorld: MyIO[Unit] = for {
  _ <- hello
  _ <- world
} yield ()

helloWorld.runUnsafe()