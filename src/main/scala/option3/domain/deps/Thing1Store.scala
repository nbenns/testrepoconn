package option3.domain.deps

import io.github.gaelrenoux.tranzactio.doobie.Connection
import option3.domain.model.{DomainError, Thing1}
import zio.ZIO

trait Thing1Store {
  def get(id: String): ZIO[Connection, DomainError, Thing1]
  def update(s: Thing1): ZIO[Connection, DomainError, Unit]
}

object Thing1Store {

}