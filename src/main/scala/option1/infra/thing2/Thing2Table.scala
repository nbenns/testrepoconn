package option1.infra.thing2

import doobie.ConnectionIO
import doobie.implicits.toSqlInterpolator

object Thing2Table {
  def getById(id: String): ConnectionIO[Option[Thing2Row]] =
    sql"select value from Thing2s where id = $id"
      .query[Thing2Row]
      .option
}
