package me.tferraro.example

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.HttpApp
import org.http4s.armeria.server.ArmeriaServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

object Http4sArmeriaServer extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    // Combine Service Routes into an HttpApp.
    // Can also be done via a Router if you
    // want to extract segments not checked
    // in the underlying routes.
    // With Middlewares in place
    val httpApp: HttpApp[IO] = Logger.httpApp(logHeaders = true, logBody = false)(
      Http4sArmeriaRoutes.helloWorldRoutes(HelloWorld.impl[IO]).orNotFound
    )

    ArmeriaServerBuilder[IO]
      .bindHttp(8080)
      .withHttpApp("/http", httpApp)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
