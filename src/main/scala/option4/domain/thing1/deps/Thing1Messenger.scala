package option4.domain.thing1.deps

import option4.domain.model.DomainError
import zio.{IO, ZIO}

trait Thing1Messenger {
  def sayhi(): IO[DomainError, Unit]
}

object Thing1Messenger {
  def sayhi(): ZIO[Thing1Messenger, DomainError, Unit] =
    ZIO.serviceWithZIO[Thing1Messenger](_.sayhi())
}
