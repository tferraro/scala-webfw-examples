package me.tferraro.example.api

import cats.effect.IO
import io.circe.generic.encoding.DerivedAsObjectEncoder.deriveEncoder
import me.tferraro.example.models.Greeting
import org.http4s._
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.io._

object HelloWorldHttp {
  def routes(): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name =>
      Ok(Greeting(name))
  }
}