import sbt._

object Dependencies {
  lazy val webFramework = Seq(
    "org.http4s"           %% "http4s-armeria-server" % "0.3.0",
    "org.http4s"           %% "http4s-circe"          % "0.23.11",
    "org.http4s"           %% "http4s-dsl"            % "0.23.11",
    "io.circe"             %% "circe-generic"         % "0.14.1",
    "org.scalameta"        %% "svm-subs"              % "20.2.0",
  )

  lazy val grpcDependencies = Seq(
    "com.linecorp.armeria" %  "armeria-grpc" % "1.16.0",
    "com.linecorp.armeria" %% "armeria-scalapb" % "1.16.0",
    //"com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
    "com.thesamet.scalapb.common-protos" %% "proto-google-common-protos-scalapb_0.11" % "2.5.0-2" % "protobuf",
    "com.thesamet.scalapb.common-protos" %% "proto-google-common-protos-scalapb_0.11" % "2.5.0-2",
    "io.grpc" % "grpc-netty" % "1.46.0"
  )

  lazy val testDependencies = Seq(
    "org.scalatest" %% "scalatest"           % "3.2.12",
    "org.scalameta" %% "munit"               % "0.7.29",
    "org.typelevel" %% "munit-cats-effect-3" % "1.0.7"
  )

  lazy val runtimeDependencies = Seq(
    "ch.qos.logback" % "logback-classic" % "1.2.11"
  )
}
