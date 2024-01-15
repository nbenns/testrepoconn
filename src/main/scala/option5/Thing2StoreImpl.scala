package option5

import doobie.ConnectionIO
import doobie.free.connection
import option5.domain.deps.Thing2Store
import option5.domain.model.{DomainError, Thing2}
import option5.infra.thing2.Thing2Table
import zio.{ULayer, ZLayer}

class Thing2StoreImpl extends Thing2Store {
  override def get(id: String): ConnectionIO[Thing2] =
    Thing2Table.getById(id)
      .map(optrow => optrow.map(row => Thing2(row.value)))
      .flatMap(_.fold[ConnectionIO[Thing2]](connection.raiseError(DomainError.SomethingNotFound(id)))(connection.pure))
}

object Thing2StoreImpl {
  val layer: ULayer[Thing2Store] = ZLayer.succeed(new Thing2StoreImpl)
}