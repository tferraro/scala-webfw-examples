package me.tferraro.example

import cats.effect.kernel.Resource
import cats.effect.std.Dispatcher
import cats.effect.{ExitCode, IO, IOApp}
import com.linecorp.armeria.common.scalapb.ScalaPbJsonMarshaller
import com.linecorp.armeria.server.cors.CorsService
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.server.logging.LoggingService
import me.tferraro.example.api.{HelloWorldGrpc, HelloWorldHttp}
import me.tferraro.example.grpc.hello.HelloServiceFs2Grpc
import org.http4s.HttpApp
import org.http4s.armeria.server.{ArmeriaServer, ArmeriaServerBuilder}
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

import scala.concurrent.duration.Duration

object Server extends IOApp {
  /** http boostrapped API using http4s */
  def restApi(): HttpApp[IO] =
    Logger.httpApp(logHeaders = true, logBody = false)(
      HelloWorldHttp.routes().orNotFound
    )

  /** gRPC boostrapped API using fs2-grpc*/
  def grpcApi(dispatcher: Dispatcher[IO]): GrpcService =
      GrpcService.builder()
      .addService(HelloServiceFs2Grpc.bindService(dispatcher, HelloWorldGrpc))
      // Register `ScalaPbJsonMarshaller` to support gRPC JSON format
      .jsonMarshallerFactory(_ => ScalaPbJsonMarshaller())
      .enableUnframedRequests(true)
      .build()

  /**
    * Creates an Armeria server with both rest & grpc endpoints
    * It also supports a /docs endpoint with the registered grpc ones.
    */
  def armeriaServer(): Resource[IO, ArmeriaServer] =
    for {
      dispatcher <- Dispatcher[IO]
      server <- {
        ArmeriaServerBuilder[IO]
          .bindHttp(8080)
          .withIdleTimeout(Duration.Zero)
          .withRequestTimeout(Duration.Zero)
          .withHttpApp("/rest", restApi())
          .withHttpServiceUnder("/grpc", grpcApi(dispatcher))
          .withDecorator(CorsService.builderForAnyOrigin().newDecorator())
          .withDecorator(LoggingService.newDecorator())
          // TODO Understand how to do it for /rest endpoints
          // Add DocService for browsing the list of gRPC services and
          // invoking a service operation from a web form.
          // See https://armeria.dev/docs/server-docservice for more information.
          .withHttpServiceUnder("/docs", new DocService())
          .withGracefulShutdownTimeout(Duration.Zero, Duration.Zero)
          .resource
      }
    } yield server

  def run(args: List[String]): IO[ExitCode] =
    armeriaServer().use(_ => IO.never).as(ExitCode.Success)
}
