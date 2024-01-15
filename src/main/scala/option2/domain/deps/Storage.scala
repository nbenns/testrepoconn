package option2.domain.deps

import option2.domain.model.DomainError
import zio._

trait Storage {
  type Txn
  def transact[R: Tag, A](z: ZIO[Txn with R, DomainError, A]): ZIO[R, DomainError, A]
}

object Storage {
  type WithTxn[T] = Storage { type Txn = T }
}