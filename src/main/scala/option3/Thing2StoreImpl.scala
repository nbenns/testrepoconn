package option3

import io.github.gaelrenoux.tranzactio.doobie.{Connection, tzio}
import option3.domain.deps.Thing2Store
import option3.domain.model.{DomainError, Thing2}
import option3.infra.thing2.Thing2Table
import zio.{ULayer, ZIO, ZLayer}

class Thing2StoreImpl extends Thing2Store {
  override def get(id: String): ZIO[Connection, DomainError, Thing2] =
    tzio { Thing2Table.getById(id) }
      .map(optrow => optrow.map(row => Thing2(row.value)))
      .mapError(e => DomainError.RepoError(e))
      .someOrFail(DomainError.SomethingNotFound(id))
}

object Thing2StoreImpl {
  val layer: ULayer[Thing2Store] = ZLayer.succeed(new Thing2StoreImpl)
}