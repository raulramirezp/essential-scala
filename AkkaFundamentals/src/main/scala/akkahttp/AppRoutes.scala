package akkahttp

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
trait AppRoutes {

  import JsonFormats._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  private val unsafeInMemoryDB: scala.collection.mutable.Map[UUID, User] = new scala.collection.mutable.HashMap[UUID, User]()

  private def getUsers(): Future[Users] =
    Future(Users(unsafeInMemoryDB.values.toList))

  private def createUser(user: User): Future[Option[UserCreated]] =
    Future{
      val userID = UUID.randomUUID()
      unsafeInMemoryDB.put(userID, user)
      Some(UserCreated(userID.toString))
    }

  private def getUser(uuid: UUID): Future[Option[User]] =
    Future(unsafeInMemoryDB.get(uuid))

  private def deleteUser(uuid: UUID): Future[Option[User]] =
    Future(unsafeInMemoryDB.remove(uuid))

  val userRoutes: Route =
    pathPrefix("users") {
      concat(
        //#users-get-delete
        pathEnd {
          concat(
            get {
              complete(getUsers())
            },
            post {
              entity(as[User]) { user =>
                onSuccess(createUser(user)) { performed =>
                  complete((StatusCodes.Created, performed))
                }
              }
            })
        },
        //#users-get-delete
        //#users-get-post
        path(Segment) { uuid =>
          concat(
            get {
              //#retrieve-user-info
              rejectEmptyResponse {
                onSuccess(getUser(UUID.fromString(uuid))) { response =>
                  complete(response)
                }
              }
              //#retrieve-user-info
            },
            delete {
              //#users-delete-logic
              onSuccess(deleteUser(UUID.fromString(uuid))) { performed =>
                complete((StatusCodes.OK, performed))
              }
              //#users-delete-logic
            })
        })
      //#users-get-delete
    }
  //#all-routes
}
