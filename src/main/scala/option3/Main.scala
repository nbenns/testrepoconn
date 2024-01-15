package option3

import option3.domain.DomainLogic
import option3.infra.DatabaseBuilder
import zio.Console.printLine
import zio._

object Main extends ZIOAppDefault {
  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] =
    DomainLogic
      .updateSomething("1")
      .tapError(printLine(_))
      .provide(DatabaseBuilder.layer, Thing1StoreImpl.layer, Thing2StoreImpl.layer, MessengerImpl.layer, DomainLogic.layer)
}