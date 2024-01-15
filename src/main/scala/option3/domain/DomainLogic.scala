package option3.domain

import io.github.gaelrenoux.tranzactio.doobie.Database
import option3.domain.deps.{Messenger, Thing1Store, Thing2Store}
import option3.domain.model.{DomainError, Thing1}
import zio.Console.printLine
import zio.{IO, ZIO, ZLayer}

class DomainLogic(
  database: Database,
  thing1Store: Thing1Store,
  thing2Store: Thing2Store,
  messenger: Messenger
) {
  def updateSomething(id: String): IO[DomainError, Unit] =
    database.transaction {
      for {
        thing1 <- thing1Store.get(id)
        _ <- printLine(s"Got $thing1").ignore

        thing2 <- thing2Store.get(id)
        _ <- printLine(s"Got $thing2").ignore

        updated = Thing1(thing1.value + 1)

        _ <- thing1Store.update(updated)
        _ <- printLine(s"Stored $updated").ignore

        _ <- messenger.sayhi()
      } yield ()
    }
    .mapError {
      case Left(value) => DomainError.RepoError(value)
      case Right(value) => value
    }
}

object DomainLogic {
  def updateSomething(id: String): ZIO[DomainLogic, DomainError, Unit] =
    ZIO.serviceWithZIO(_.updateSomething(id))

  val layer: ZLayer[Database with Thing1Store with Thing2Store with Messenger, Nothing, DomainLogic] =
    ZLayer.fromFunction(new DomainLogic(_, _, _, _))
}
