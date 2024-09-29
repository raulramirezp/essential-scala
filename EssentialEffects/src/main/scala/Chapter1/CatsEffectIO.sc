import cats.effect.IO
//import cats.effect.unsafe.implicits.global

val hw: IO[Unit] = IO.delay(println("Hello world!"))
//hw.attempt
//hw.unsafeRunAndForget()
hw.unsafeRunAsyncAndForget()


