package option2.domain

import option2.domain.deps.{Messenger, Storage, Thing1Store, Thing2Store}
import option2.domain.model.{DomainError, Thing1}
import zio.Console.printLine
import zio.{IO, Tag, ZIO, ZLayer}

class DomainLogic[T](
  storage: Storage.WithTxn[T],
  thing1Store: Thing1Store.WithTxn[T],
  thing2Store: Thing2Store.WithTxn[T],
  messenger: Messenger
) {
  def updateSomething(id: String): IO[DomainError, Unit] = storage.transact {
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
}

object DomainLogic {
  def updateSomething[T: Tag](id: String): ZIO[DomainLogic[T], DomainError, Unit] =
    ZIO.serviceWithZIO(_.updateSomething(id))

  def layer[T: Tag]: ZLayer[Storage.WithTxn[T] with Thing1Store.WithTxn[T] with Thing2Store.WithTxn[T] with Messenger, Nothing, DomainLogic[T]] =
    ZLayer.fromFunction(new DomainLogic[T](_, _, _, _))
}
