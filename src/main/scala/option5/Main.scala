package option5

import option5.domain.DomainLogic
import zio.Console.printLine
import zio._

object Main extends ZIOAppDefault {
  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] =
    DomainLogic
      .updateSomething("1")
      .tapError(printLine(_))
      .provide(StorageImpl.layer, Thing1StoreImpl.layer, Thing2StoreImpl.layer, MessengerImpl.layer, DomainLogic.layer)
}