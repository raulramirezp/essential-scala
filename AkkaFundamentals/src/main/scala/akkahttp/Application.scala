package akkahttp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{Directive, Route}
import com.typesafe.config.ConfigFactory
import monix.eval.Task
import monix.execution.Scheduler

import scala.util.{Failure, Success}
object Application extends App with AppRoutes {
  private def startHttpServer(routes: Route)(implicit system: ActorSystem): Unit = {
    // Akka HTTP still needs a classic ActorSystem to start
    implicit lazy val httpScheduler: Scheduler = Scheduler.io("http-outbound-scheduler")
    val task = Task.pure(4)
    val futureBinding =
      Http()
        .newServerAt("localhost", 8080)
        .bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }


  startHttpServer(userRoutes)(ActorSystem("Http", ConfigFactory.load()))

}
