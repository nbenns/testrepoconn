package option5.domain.model

sealed abstract class DomainError(message: String, throwable: Throwable) extends Throwable(message, throwable) with Product with Serializable

object DomainError {
  case class SomethingNotFound(id: String) extends DomainError(s"Not found $id", null)
  case class RepoError(underlying: Throwable) extends DomainError(underlying.getMessage, underlying)
}
