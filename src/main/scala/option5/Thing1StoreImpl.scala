package option5

import doobie.ConnectionIO
import doobie.free.connection
import option5.domain.deps.Thing1Store
import option5.domain.model.{DomainError, Thing1}
import option5.infra.thing1.{Thing1Row, Thing1Table}
import zio.{ULayer, ZLayer}

class Thing1StoreImpl extends Thing1Store {
  override def get(id: String): ConnectionIO[Thing1] =
    Thing1Table
      .getById(id)
      .map{optrow => optrow.map(row => Thing1(row.value))}
      .flatMap(_.fold[ConnectionIO[Thing1]](connection.raiseError(DomainError.SomethingNotFound(id)))(connection.pure))

  override def update(s: Thing1): ConnectionIO[Unit] =
    Thing1Table.update(Thing1Row(s.value))
      .map(row => Thing1(row.value))
}

object Thing1StoreImpl {
  val layer: ULayer[Thing1Store] = ZLayer.succeed(new Thing1StoreImpl)
}
