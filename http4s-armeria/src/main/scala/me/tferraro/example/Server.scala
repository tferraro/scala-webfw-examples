package me.tferraro.example

import cats.effect.std.Dispatcher
import cats.effect.{ExitCode, IO, IOApp}
import com.linecorp.armeria.common.scalapb.ScalaPbJsonMarshaller
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.server.logging.LoggingService
import me.tferraro.example.grpc.HelloServiceImpl
import me.tferraro.example.grpc.hello.HelloServiceFs2Grpc
import me.tferraro.example.rest.{HelloWorld, Http4sRoutes}
import org.http4s.HttpApp
import org.http4s.armeria.server.ArmeriaServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

import scala.concurrent.duration.Duration

object Server extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    // Combine Service Routes into an HttpApp.
    // Can also be done via a Router if you
    // want to extract segments not checked
    // in the underlying routes.
    // With Middlewares in place
    val httpApp: HttpApp[IO] = Logger.httpApp(logHeaders = true, logBody = false)(
      Http4sRoutes.helloWorldRoutes(HelloWorld.impl[IO]).orNotFound
    )
    // Build gRPC service
    val server = for {
      dispacher <- Dispatcher[IO]
      server <- {
        val grpcService = GrpcService.builder()
          .addService(HelloServiceFs2Grpc.bindService(dispacher, new HelloServiceImpl))
          // Register `ScalaPbJsonMarshaller` to support gRPC JSON format
          .jsonMarshallerFactory(_ => ScalaPbJsonMarshaller())
          .enableUnframedRequests(true)
          .build()

        ArmeriaServerBuilder[IO]
          .bindHttp(8080)
          .withIdleTimeout(Duration.Zero)
          .withRequestTimeout(Duration.Zero)
          .withHttpApp("/rest", httpApp)
          .withHttpServiceUnder("/grpc", grpcService)
          .withDecorator(LoggingService.newDecorator())
          // Add DocService for browsing the list of gRPC services and
          // invoking a service operation from a web form.
          // See https://armeria.dev/docs/server-docservice for more information.
          .withHttpServiceUnder("/docs", new DocService())
          .withGracefulShutdownTimeout(Duration.Zero, Duration.Zero)
          .resource
      }
    } yield server

    server.use(_ => IO.never).as(ExitCode.Success)
  }
}
