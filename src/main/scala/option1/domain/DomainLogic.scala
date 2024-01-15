package option1.domain

import option1.domain.deps.{Messenger, Storage}
import option1.domain.model.{DomainError, Thing1}
import zio.Console.printLine
import zio.{IO, ZIO, ZLayer}

class DomainLogic(
  storage: Storage,
  messenger: Messenger
) {
  def updateSomething(id: String): IO[DomainError, Unit] = storage.transact {
    for {
      thing1 <- storage.thing1s.get(id)
      _ <- printLine(s"Got $thing1").ignore

      thing2 <- storage.thing2s.get(id)
      _ <- printLine(s"Got $thing2").ignore

      updated = Thing1(thing1.value + 1)

      _ <- storage.thing1s.update(updated)
      _ <- printLine(s"Stored $updated").ignore

      _ <- messenger.sayhi()
    } yield ()
  }
}

object DomainLogic {
  def updateSomething(id: String): ZIO[DomainLogic, DomainError, Unit] =
    ZIO.serviceWithZIO(_.updateSomething(id))

  val layer: ZLayer[Storage with Messenger, Nothing, DomainLogic] =
    ZLayer.fromFunction(new DomainLogic(_, _))
}
