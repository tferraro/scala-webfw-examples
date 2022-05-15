package me.tferraro.example.grpc

import cats.effect.IO
import io.grpc.Metadata
import fs2._
import me.tferraro.example.grpc.hello.{HelloReply, HelloRequest, HelloServiceFs2Grpc}

class HelloServiceImpl extends HelloServiceFs2Grpc[IO, Metadata] {

  override def unary(request: HelloRequest, ctx: Metadata): IO[HelloReply] =
    IO(HelloReply(s"Hello ${request.name}!"))

  override def serverStreaming(request: HelloRequest, ctx: Metadata): Stream[IO, HelloReply] =
    Stream
      .range(1, 6)
      .map(i => s"Hello ${request.name} $i!")
      .map(HelloReply(_))

  override def clientStreaming(request: Stream[IO, HelloRequest], ctx: Metadata): IO[HelloReply] =
    request
      .map(_.name)
      .compile
      .toVector
      .map(_.mkString(", "))
      .map(all => HelloReply(s"Hello $all!"))

  override def bidiStreaming(
                              request: Stream[IO, HelloRequest],
                              ctx: Metadata): Stream[IO, HelloReply] =
    request.map(req => HelloReply(s"Hello ${req.name}!"))
}
