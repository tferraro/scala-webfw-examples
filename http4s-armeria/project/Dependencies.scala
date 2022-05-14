import sbt._

object Dependencies {
  lazy val webFramework = Seq(
    "org.http4s"    %% "http4s-armeria-server" % "0.3.0",
    "org.http4s"    %% "http4s-circe"          % "0.23.11",
    "org.http4s"    %% "http4s-dsl"            % "0.23.11",
    "io.circe"      %% "circe-generic"         % "0.14.1",
    "org.scalameta" %% "svm-subs"              % "20.2.0"
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
