package option3

import io.github.gaelrenoux.tranzactio.doobie.{Connection, tzio}
import option3.domain.deps.Thing1Store
import option3.domain.model.{DomainError, Thing1}
import option3.infra.thing1.{Thing1Row, Thing1Table}
import zio.{ULayer, ZIO, ZLayer}

class Thing1StoreImpl extends Thing1Store {
  type Txn = Connection
  override def get(id: String): ZIO[Txn, DomainError, Thing1] =
    tzio(Thing1Table.getById(id))
      .map(optrow => optrow.map(row => Thing1(row.value)))
      .mapError(e => DomainError.RepoError(e))
      .someOrFail(DomainError.SomethingNotFound(id))

  override def update(s: Thing1): ZIO[Txn, DomainError, Unit] =
    tzio { Thing1Table.update(Thing1Row(s.value)) }
      .map(row => Thing1(row.value))
      .mapError(e => DomainError.RepoError(e))
      .unit
}

object Thing1StoreImpl {
  val layer: ULayer[Thing1Store] = ZLayer.succeed(new Thing1StoreImpl)
}
