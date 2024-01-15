package option2.domain.deps

import option2.domain.model.{DomainError, Thing2}
import zio.ZIO

trait Thing2Store {
  type Txn
  def get(id: String): ZIO[Txn, DomainError, Thing2]
}

object Thing2Store {
  type WithTxn[T] = Thing2Store { type Txn = T }
}
