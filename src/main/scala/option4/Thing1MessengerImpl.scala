package option4

import option4.domain.model.DomainError
import option4.domain.thing1.deps.Thing1Messenger
import zio.Console.printLine
import zio.{IO, ULayer, ZLayer}

class Thing1MessengerImpl extends Thing1Messenger {
  override def sayhi(): IO[DomainError, Unit] = printLine("hi").ignore
}

object Thing1MessengerImpl {
  val layer: ULayer[Thing1Messenger] = ZLayer.succeed(new Thing1MessengerImpl)
}
