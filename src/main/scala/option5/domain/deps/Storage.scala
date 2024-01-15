package option5.domain.deps

import cats.~>
import doobie.ConnectionIO
import option5.domain.model.DomainError
import zio._

trait Storage {
  def transact[A](z: (Task ~> ConnectionIO) => ConnectionIO[A]): IO[DomainError, A]
}
