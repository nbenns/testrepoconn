package option1

import option1.domain.DomainLogic
import zio.Console.printLine
import zio._

object Main extends ZIOAppDefault {
  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] =
    DomainLogic
      .updateSomething("1")
      .tapError(printLine(_))
      .provide(StorageImpl.layer, MessengerImpl.layer, DomainLogic.layer)
}