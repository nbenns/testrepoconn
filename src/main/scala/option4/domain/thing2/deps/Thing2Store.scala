package option4.domain.thing2.deps

import option4.domain.model.{DomainError, Thing2}
import zio.ZIO

trait Thing2Store {
  type Txn
  def get(id: String): ZIO[Txn, DomainError, Thing2]
}
