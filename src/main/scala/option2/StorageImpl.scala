package option2

import cats.effect.kernel.Resource
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import doobie.h2.H2Transactor
import doobie.util.ExecutionContexts
import io.github.gaelrenoux.tranzactio.doobie.{Connection, Database, tzio}
import option2.domain.deps.Storage
import option2.domain.model.DomainError
import zio.interop.catz._
import zio.{Tag, Task, ZIO, ZLayer}

class StorageImpl(database: Database) extends Storage {

  override type Txn = Connection

  override def transact[R: Tag, A](z: ZIO[Txn with R, DomainError, A]): ZIO[R, DomainError, A] =
    database.transaction[R, DomainError, A](z)
      .mapError {
        case Left(value) => DomainError.RepoError(value)
        case Right(value) => value
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

  val layer: ZLayer[Any, Throwable, Storage.WithTxn[Connection]] =
    ZLayer.succeed(new HikariDataSource(config))
      .to(Database.fromDatasource)
      .project(new StorageImpl(_))
}

