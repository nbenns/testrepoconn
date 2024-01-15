package option4

import io.github.gaelrenoux.tranzactio.doobie.Connection
import option4.domain.thing1.Thing1Logic
import option4.infra.DatabaseBuilder
import zio.Console.printLine
import zio._

object Main extends ZIOAppDefault {
  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] =
    Thing1Logic
      .updateSomething("1")
      .tapError(printLine(_))
      .provide(DatabaseBuilder.layer, Thing1StoreImpl.layer, Thing1MessengerImpl.layer, Thing1Logic.layer)
}