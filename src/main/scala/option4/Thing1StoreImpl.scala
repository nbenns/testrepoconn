package option4

import doobie.util.transactor
import io.github.gaelrenoux.tranzactio.doobie.{Connection, Database, tzio}
import option4.domain.model.{DomainError, Thing1, Thing2}
import option4.domain.thing1.deps.Thing1Store
import option4.infra.thing1.{Thing1Row, Thing1Table}
import option4.infra.thing2.Thing2Table
import zio._

class Thing1StoreImpl(database: Database) extends Thing1Store {
  type Txn = Connection

  override def transact[R: Tag, A](z: ZIO[Txn with R, DomainError, A]): ZIO[R, DomainError, A] =
    database.transaction[R, DomainError, A](z)
      .mapError {
        case Left(value) => DomainError.RepoError(value)
        case Right(value) => value
      }

  override def get(id: String): ZIO[Txn, DomainError, Thing1] =
    tzio(Thing1Table.getById(id))
      .map(optrow => optrow.map(row => Thing1(row.value)))
      .mapError(e => DomainError.RepoError(e))
      .someOrFail(DomainError.SomethingNotFound(id))

  override def getThing2(id: String): ZIO[transactor.Transactor[Task], DomainError, Thing2] =
    tzio { Thing2Table.getById(id) }
      .map(optrow => optrow.map(row => Thing2(row.value)))
      .mapError(e => DomainError.RepoError(e))
      .someOrFail(DomainError.SomethingNotFound(id))

  override def update(s: Thing1): ZIO[Txn, DomainError, Unit] =
    tzio { Thing1Table.update(Thing1Row(s.value)) }
      .map(row => Thing1(row.value))
      .mapError(e => DomainError.RepoError(e))
      .unit
}

object Thing1StoreImpl {
  val layer: ZLayer[Database, Nothing, Thing1Store] = ZLayer.fromFunction(new Thing1StoreImpl(_))
}
