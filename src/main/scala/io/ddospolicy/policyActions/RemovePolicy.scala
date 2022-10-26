package io.ddospolicy.policyActions

import cats.Applicative
import cats.implicits._
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe._

trait RemovePolicy[F[_]]{
  def removePolicy(n: RemovePolicy.PolicyName): F[RemovePolicy.RemovePolicyResponse]
}

object RemovePolicy {
  implicit def apply[F[_]](implicit ev: RemovePolicy[F]): RemovePolicy[F] = ev

  final case class PolicyName(name: String) extends AnyVal
  final case class RemovePolicyResponse(response: String) extends AnyVal

  object RemovePolicyResponse {
    implicit val RemovePolicyEncoderEncoder: Encoder[RemovePolicyResponse] = new Encoder[RemovePolicyResponse] {
      final def apply(a: RemovePolicyResponse): Json = Json.obj(
        ("Status", Json.fromString(a.response)),
      )
    }
    implicit def greetingEntityEncoder[F[_]]: EntityEncoder[F, RemovePolicyResponse] =
      jsonEncoderOf[F, RemovePolicyResponse]
  }

  def impl[F[_]: Applicative]: RemovePolicy[F] = new RemovePolicy[F]{
    def removePolicy(n: RemovePolicy.PolicyName): F[RemovePolicy.RemovePolicyResponse] =
      RemovePolicyResponse(s"Removed Policy with name: [${n.name}]").pure[F]
  }
}
