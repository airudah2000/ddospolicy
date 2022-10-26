package io.ddospolicy.policyActions

import cats.Applicative
import cats.implicits._
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe._

trait ListPolicies[F[_]]{
  def listPolicies: F[ListPolicies.ListPoliciesResponse]
}

object ListPolicies {
  implicit def apply[F[_]](implicit ev: CreatePolicy[F]): CreatePolicy[F] = ev

  final case class ListPoliciesResponse(response: String) extends AnyVal

  object CreatePolicyResponse {
    implicit val createPolicyEncoderEncoder: Encoder[ListPoliciesResponse] = new Encoder[ListPoliciesResponse] {
      final def apply(a: ListPoliciesResponse): Json = Json.obj(
        ("Status", Json.fromString(a.response.mkString)),
      )
    }
    implicit def greetingEntityEncoder[F[_]]: EntityEncoder[F, ListPoliciesResponse] =
      jsonEncoderOf[F, ListPoliciesResponse]
  }

  def impl[F[_]: Applicative]: ListPolicies[F] = new ListPolicies[F]{
    def listPolicies: F[ListPolicies.ListPoliciesResponse] =
      ListPoliciesResponse("Not currently implemented").pure[F]
  }
}
