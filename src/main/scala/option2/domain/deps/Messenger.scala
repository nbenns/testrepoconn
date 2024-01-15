package option2.domain.deps

import option2.domain.model.DomainError
import zio.{IO, ZIO}

trait Messenger {
  def sayhi(): IO[DomainError, Unit]
}

object Messenger {
  def sayhi(): ZIO[Messenger, DomainError, Unit] =
    ZIO.serviceWithZIO[Messenger](_.sayhi())
}
