package io.ddospolicy

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  def run(args: List[String]) =
    DdospolicyServer.stream[IO].compile.drain.as(ExitCode.Success)
}
