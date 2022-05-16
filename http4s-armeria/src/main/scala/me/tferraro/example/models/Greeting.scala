package me.tferraro.example.models

import me.tferraro.example.grpc.hello.HelloReply

case class Greeting(message: String) {
  def toReply: HelloReply = HelloReply(s"Hello $message!")
}
