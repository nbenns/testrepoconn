package option4.domain.thing1

import option4.domain.model.{DomainError, Thing1}
import option4.domain.thing1.deps.{Thing1Messenger, Thing1Store}
import zio.Console.printLine
import zio.{IO, Tag, ZIO, ZLayer}

class Thing1Logic(
  thing1Store: Thing1Store,
  messenger: Thing1Messenger
) {
  def updateThing1(id: String): IO[DomainError, Unit] = thing1Store.transact {
    for {
      thing1 <- thing1Store.get(id)
      _ <- printLine(s"Got $thing1").ignore

      thing2 <- thing1Store.getThing2(id)
      _ <- printLine(s"Got $thing2").ignore

      updated = Thing1(thing1.value + 1)

      _ <- thing1Store.update(updated)
      _ <- printLine(s"Stored $updated").ignore

      _ <- messenger.sayhi()
    } yield ()
  }
}

object Thing1Logic {
  def updateSomething(id: String): ZIO[Thing1Logic, DomainError, Unit] =
    ZIO.serviceWithZIO(_.updateThing1(id))

  def layer[T: Tag]: ZLayer[Thing1Store with Thing1Messenger, Nothing, Thing1Logic] =
    ZLayer.fromFunction(new Thing1Logic(_, _))
}
