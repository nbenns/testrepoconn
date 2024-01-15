package option5.domain.deps

import doobie.ConnectionIO
import option5.domain.model.Thing1

trait Thing1Store {
  def get(id: String): ConnectionIO[Thing1]
  def update(s: Thing1): ConnectionIO[Unit]
}

object Thing1Store {

}