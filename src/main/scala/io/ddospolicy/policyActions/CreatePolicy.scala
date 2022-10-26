package io.ddospolicy.policyActions

import cats.Applicative
import cats.implicits._
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe._

trait CreatePolicy[F[_]]{
  def createPolicy(n: CreatePolicy.PolicyName): F[CreatePolicy.CreatePolicyResponse]
}

object CreatePolicy {
  implicit def apply[F[_]](implicit ev: CreatePolicy[F]): CreatePolicy[F] = ev

  final case class PolicyName(name: String) extends AnyVal
  final case class CreatePolicyResponse(response: String) extends AnyVal

  object CreatePolicyResponse {
    implicit val createPolicyEncoderEncoder: Encoder[CreatePolicyResponse] = new Encoder[CreatePolicyResponse] {
      final def apply(a: CreatePolicyResponse): Json = Json.obj(
        ("Status", Json.fromString(a.response)),
      )
    }
    implicit def greetingEntityEncoder[F[_]]: EntityEncoder[F, CreatePolicyResponse] =
      jsonEncoderOf[F, CreatePolicyResponse]
  }

  def impl[F[_]: Applicative]: CreatePolicy[F] = new CreatePolicy[F]{
    def createPolicy(n: CreatePolicy.PolicyName): F[CreatePolicy.CreatePolicyResponse] =
      CreatePolicyResponse(s"Created Policy with name: [${n.name}]").pure[F]
  }
}
