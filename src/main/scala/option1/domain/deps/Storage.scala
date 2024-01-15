package option1.domain.deps

import option1.domain.model.{DomainError, Thing1, Thing2}
import zio.{Tag, ZIO}

trait Storage {
  type Txn
  def transact[R: Tag, A](z: ZIO[Txn with R, DomainError, A]): ZIO[R, DomainError, A]

  val thing1s: Thing1Store
  val thing2s: Thing2Store

  trait Thing1Store {
    def get(id: String): ZIO[Txn, DomainError, Thing1]
    def update(s: Thing1): ZIO[Txn, DomainError, Unit]
  }

  trait Thing2Store {
    def get(id: String): ZIO[Txn, DomainError, Thing2]
  }
}

object Storage {

}
