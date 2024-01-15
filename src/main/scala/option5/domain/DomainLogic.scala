package option5.domain

import option5.domain.deps.{Messenger, Storage, Thing1Store, Thing2Store}
import option5.domain.model.{DomainError, Thing1}
import zio.Console.printLine
import zio.{IO, ZIO, ZLayer}

class DomainLogic(
  storage: Storage,
  thing1Store: Thing1Store,
  thing2Store: Thing2Store,
  messenger: Messenger
) {
  def updateSomething(id: String): IO[DomainError, Unit] = storage.transact { taskToConnectionIO =>
    for {
      thing1 <- thing1Store.get(id)
      _ <- taskToConnectionIO(printLine(s"Got $thing1"))

      thing2 <- thing2Store.get(id)
      _ <- taskToConnectionIO(printLine(s"Got $thing2"))

      updated = Thing1(thing1.value + 1)

      _ <- thing1Store.update(updated)
      _ <- taskToConnectionIO(printLine(s"Stored $updated"))

      _ <- taskToConnectionIO(messenger.sayhi())
    } yield ()
  }
}

object DomainLogic {
  def updateSomething(id: String): ZIO[DomainLogic, DomainError, Unit] =
    ZIO.serviceWithZIO(_.updateSomething(id))

  val layer: ZLayer[Storage with Thing1Store with Thing2Store with Messenger, Nothing, DomainLogic] =
    ZLayer.fromFunction(new DomainLogic(_, _, _, _))
}
