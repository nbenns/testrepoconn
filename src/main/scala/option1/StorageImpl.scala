package option1

import cats.effect.kernel.Resource
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import doobie.h2.H2Transactor
import doobie.util.ExecutionContexts
import io.github.gaelrenoux.tranzactio.doobie.{Database, tzio, Connection => DoobieConnection}
import option1.domain.deps.Storage
import option1.domain.model.{DomainError, Thing1, Thing2}
import option1.infra.thing1.{Thing1Row, Thing1Table}
import option1.infra.thing2.Thing2Table
import zio.interop.catz._
import zio.{Tag, Task, ZIO, ZLayer}

class StorageImpl(database: Database) extends Storage {

  override type Txn = DoobieConnection

  override def transact[R: Tag, A](z: ZIO[Txn with R, DomainError, A]): ZIO[R, DomainError, A] =
    database.transaction[R, DomainError, A](z)
      .mapError {
        case Left(value) => DomainError.RepoError(value)
        case Right(value) => value
      }

  override val thing1s: Thing1Store = new Thing1StoreImpl
  override val thing2s: Thing2Store = new Thing2StoreImpl

  class Thing1StoreImpl extends Thing1Store {
    override def get(id: String): ZIO[Txn, DomainError, Thing1] =
      tzio { Thing1Table.getById(id) }
        .map(optrow => optrow.map(row => Thing1(row.value)))
        .mapError(e => DomainError.RepoError(e))
        .someOrFail(DomainError.SomethingNotFound(id))

    override def update(s: Thing1): ZIO[Txn, DomainError, Unit] =
      tzio { Thing1Table.update(Thing1Row(s.value)) }
        .map(row => Thing1(row.value))
        .mapError(e => DomainError.RepoError(e))
        .unit
  }

  class Thing2StoreImpl extends Thing2Store {
    override def get(id: String): ZIO[Txn, DomainError, Thing2] =
      tzio { Thing2Table.getById(id) }
        .map(optrow => optrow.map(row => Thing2(row.value)))
        .mapError(e => DomainError.RepoError(e))
        .someOrFail(DomainError.SomethingNotFound(id))
  }
}

object StorageImpl {
  private val transactor: Resource[Task, H2Transactor[Task]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool[Task](32) // our connect EC
      xa <- H2Transactor.newH2Transactor[Task](
        "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", // connect URL
        "sa", // username
        "", // password
        ce, // await connection here
      )
    } yield xa

  private val config = {
    val config = new HikariConfig()

    config.setJdbcUrl(s"jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
    config.setUsername("sa")
    config.setPassword("")
    //      config.setMaximumPoolSize(dbConfig.maxActiveConnections)

    config
  }

  val layer: ZLayer[Any, Throwable, Storage] =
    ZLayer.succeed(new HikariDataSource(config))
      .to(Database.fromDatasource)
      .project(new StorageImpl(_))
}
