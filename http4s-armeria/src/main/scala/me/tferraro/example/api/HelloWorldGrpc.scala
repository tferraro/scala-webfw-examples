package me.tferraro.example.api

import cats.effect.IO
import io.grpc.Metadata
import me.tferraro.example.grpc.hello.{HelloReply, HelloRequest, HelloServiceFs2Grpc}
import me.tferraro.example.models.Greeting

object HelloWorldGrpc extends HelloServiceFs2Grpc[IO, Metadata] {
  lazy val reply: HelloRequest => HelloReply =
    (r: HelloRequest) => Greeting(r.name).toReply

  override def greet(request: HelloRequest, ctx: Metadata): IO[HelloReply] =
    IO(reply(request))
}
