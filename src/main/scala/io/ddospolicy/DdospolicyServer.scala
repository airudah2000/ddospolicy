package io.ddospolicy

import cats.effect.{Async, Resource}
import cats.syntax.all._
import com.comcast.ip4s._
import fs2.Stream
import io.ddospolicy.policyActions.{CreatePolicy, ListPolicies, RemovePolicy}
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

object DdospolicyServer {

  def stream[F[_]: Async]: Stream[F, Nothing] = {
    for {
      client <- Stream.resource(EmberClientBuilder.default[F].build)
//      helloWorldAlg = HelloWorld.impl[F]
      createPolicyAlg = CreatePolicy.impl[F]
      removePolicyAlg = RemovePolicy.impl[F]
      listPoliciesAlg = ListPolicies.impl[F]
      jokeAlg = Jokes.impl[F](client)

      // Combine Service Routes into an HttpApp.
      // Can also be done via a Router if you
      // want to extract segments not checked
      // in the underlying routes.
      httpApp = (
//        DdospolicyRoutes.helloWorldRoutes[F](helloWorldAlg) <+>
        DdospolicyRoutes.createPolicyRoutes[F](createPolicyAlg) <+>
        DdospolicyRoutes.removePolicyRoutes[F](removePolicyAlg) <+>
        DdospolicyRoutes.listPoliciesRoutes[F](listPoliciesAlg) <+>
        DdospolicyRoutes.jokeRoutes[F](jokeAlg)
      ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      exitCode <- Stream.resource(
        EmberServerBuilder.default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(finalHttpApp)
          .build >>
        Resource.eval(Async[F].never)
      )
    } yield exitCode
  }.drain
}
