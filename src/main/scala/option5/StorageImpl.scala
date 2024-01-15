package option5

import cats.~>
import doobie._
import doobie.h2.H2Transactor
import doobie.implicits._
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import option5.domain.deps.Storage
import option5.domain.model.DomainError
import zio.interop.catz._
import zio.{IO, Task, ZLayer}

class StorageImpl(xa: Transactor[Task]) extends Storage {
  override def transact[A](z: (Task ~> ConnectionIO) => ConnectionIO[A]): IO[DomainError, A] =
    WeakAsync
      .liftK[Task, ConnectionIO]
      .use(nat => z(nat).transact(xa))
      .mapError {
        case e: DomainError => e
        case e => DomainError.RepoError(e)
      }
}

object StorageImpl {
  val layer: ZLayer[Any, Throwable, Storage] =
    ZLayer.scoped {
        (
          for {
            ce <- ExecutionContexts.fixedThreadPool[Task](32) // our connect EC
            xa <- H2Transactor.newH2Transactor[Task](
              "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", // connect URL
              "sa", // username
              "", // password
              ce, // await connection here
            )
          } yield xa
        )
          .toScopedZIO
          .map(new StorageImpl(_))
    }
}

