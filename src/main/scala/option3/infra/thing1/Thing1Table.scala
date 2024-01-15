package option3.infra.thing1

import doobie.ConnectionIO
import doobie.implicits.toSqlInterpolator

object Thing1Table {
  def getById(id: String): ConnectionIO[Option[Thing1Row]] =
    sql"select value from Thing1s where id = $id"
      .query[Thing1Row]
      .option

  def update(s: Thing1Row): ConnectionIO[Thing1Row] =
    sql"update Thing1s SET value = ${s.value} WHERE id = 1"
      .query[Thing1Row]
      .unique
}
