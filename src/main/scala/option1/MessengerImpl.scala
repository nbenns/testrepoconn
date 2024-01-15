package option1

import option1.domain.deps.Messenger
import option1.domain.model.DomainError
import zio.Console.printLine
import zio.{IO, ULayer, ZLayer}

class MessengerImpl extends Messenger {
  override def sayhi(): IO[DomainError, Unit] = printLine("hi").ignore
}

object MessengerImpl {
  val layer: ULayer[Messenger] = ZLayer.succeed(new MessengerImpl)
}
