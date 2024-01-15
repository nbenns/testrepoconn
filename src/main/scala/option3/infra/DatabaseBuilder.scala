package option3.infra

import cats.effect.kernel.Resource
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import doobie.h2.H2Transactor
import doobie.util.ExecutionContexts
import io.github.gaelrenoux.tranzactio.doobie.Database
import zio.interop.catz._
import zio.{Task, ZLayer}

object DatabaseBuilder {
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

  val layer: ZLayer[Any, Throwable, Database] =
    ZLayer.succeed(new HikariDataSource(config))
      .to(Database.fromDatasource)
}
