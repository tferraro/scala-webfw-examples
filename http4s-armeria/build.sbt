import Dependencies._

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "me.tferraro"
ThisBuild / organizationName := "tferraro"

lazy val root = (project in file("."))
  .settings(
    name := "example-armeria-http4s",
    libraryDependencies ++= webFramework ++ grpcDependencies
      ++ testDependencies.map(_ % Test)
      ++ runtimeDependencies.map(_ % Runtime),
    // For IntelliJ to detect the compiled .proto classes
    sbtprotoc.ProtocPlugin.ProtobufConfig / scalaSource := sourceDirectory.value / "src_managed"
  )
  .enablePlugins(Fs2Grpc)
// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
