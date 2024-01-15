package option4.domain.thing1.deps

import option4.domain.model.{DomainError, Thing1, Thing2}
import zio.{Tag, ZIO}

trait Thing1Store {
  type Txn

  def transact[R: Tag, A](z: ZIO[Txn with R, DomainError, A]): ZIO[R, DomainError, A]

  def get(id: String): ZIO[Txn, DomainError, Thing1]
  def getThing2(id: String): ZIO[Txn, DomainError, Thing2]
  def update(s: Thing1): ZIO[Txn, DomainError, Unit]
}
