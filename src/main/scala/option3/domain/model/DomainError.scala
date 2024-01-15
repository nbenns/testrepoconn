package option3.domain.model

sealed trait DomainError extends Product with Serializable

object DomainError {
  case class SomethingNotFound(id: String) extends DomainError
  case class RepoError(underlying: Throwable) extends DomainError
}
