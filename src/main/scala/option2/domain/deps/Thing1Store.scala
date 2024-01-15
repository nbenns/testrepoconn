package option2.domain.deps

import option2.domain.model.{DomainError, Thing1}
import zio.ZIO

trait Thing1Store {
  type Txn
  def get(id: String): ZIO[Txn, DomainError, Thing1]
  def update(s: Thing1): ZIO[Txn, DomainError, Unit]
}

object Thing1Store {
  type WithTxn[T] = Thing1Store { type Txn = T }
}