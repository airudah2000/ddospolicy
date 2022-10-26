package io.ddospolicy

import cats.effect.Sync
import cats.implicits._
import io.ddospolicy.policyActions.{CreatePolicy, ListPolicies, RemovePolicy}
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object DdospolicyRoutes {

  def jokeRoutes[F[_]: Sync](J: Jokes[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "joke" =>
        for {
          joke <- J.get
          resp <- Ok(joke)
        } yield resp
    }
  }

//  def helloWorldRoutes[F[_]: Sync](H: HelloWorld[F]): HttpRoutes[F] = {
//    val dsl = new Http4sDsl[F]{}
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "hello" / name =>
//        for {
//          greeting <- H.hello(HelloWorld.Name(name))
//          resp <- Ok(greeting)
//        } yield resp
//    }
//  }

  def createPolicyRoutes[F[_]: Sync](H: CreatePolicy[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "createPolicy" / name =>
        for {
          policy <- H.createPolicy(CreatePolicy.PolicyName(name))
          resp <- Ok(policy)
        } yield resp
    }
  }

  def removePolicyRoutes[F[_]: Sync](H: RemovePolicy[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "removePolicy" / name =>
        for {
          policy: RemovePolicy.RemovePolicyResponse <- H.removePolicy(RemovePolicy.PolicyName(name))
          resp <- Ok(policy)
        } yield resp
    }
  }

  def listPoliciesRoutes[F[_]: Sync](H: ListPolicies[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "listPolicies" =>
        for {
          policies: ListPolicies.ListPoliciesResponse <- H.listPolicies
          resp <- Ok(policies.response)
        } yield resp
    }
  }
}