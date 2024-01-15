package option2

import io.github.gaelrenoux.tranzactio.doobie.Connection
import option2.domain.DomainLogic
import zio.Console.printLine
import zio._

object Main extends ZIOAppDefault {
  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] =
    DomainLogic
      .updateSomething[Connection]("1")
      .tapError(printLine(_))
      .provide(StorageImpl.layer, Thing1StoreImpl.layer, Thing2StoreImpl.layer, MessengerImpl.layer, DomainLogic.layer[Connection])
}