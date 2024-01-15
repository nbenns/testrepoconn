package option5.domain.deps

import doobie.ConnectionIO
import option5.domain.model.Thing2

trait Thing2Store {
  def get(id: String): ConnectionIO[Thing2]
}

object Thing2Store {

}
