package option3.domain.deps

import io.github.gaelrenoux.tranzactio.doobie.Connection
import option3.domain.model.{DomainError, Thing2}
import zio.ZIO

trait Thing2Store {
  def get(id: String): ZIO[Connection, DomainError, Thing2]
}

object Thing2Store {

}
