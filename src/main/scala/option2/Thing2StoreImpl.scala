package option2

import io.github.gaelrenoux.tranzactio.doobie.{tzio, Connection => DoobieConnection}
import option2.domain.deps.Thing2Store
import option2.domain.model.{DomainError, Thing2}
import option2.infra.thing2.Thing2Table
import zio.{ULayer, ZIO, ZLayer}

class Thing2StoreImpl extends Thing2Store {
  override type Txn = DoobieConnection

  override def get(id: String): ZIO[Txn, DomainError, Thing2] =
    tzio { Thing2Table.getById(id) }
      .map(optrow => optrow.map(row => Thing2(row.value)))
      .mapError(e => DomainError.RepoError(e))
      .someOrFail(DomainError.SomethingNotFound(id))
}

object Thing2StoreImpl {
  val layer: ULayer[Thing2Store.WithTxn[DoobieConnection]] = ZLayer.succeed(new Thing2StoreImpl)
}