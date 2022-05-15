addDependencyTreePlugin
addSbtPlugin("net.vonbuchholtz" % "sbt-dependency-check" % "4.1.0")
addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.1.14")
addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.10")
// Scalac options
addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.1.20")
// ScalaDoc API mapping
addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % "3.0.0")
// ScalaPB Reactor
addSbtPlugin("com.thesamet" % "sbt-protoc" % "1.0.4")
libraryDependencies += "kr.ikhoon.scalapb-reactor" %% "scalapb-reactor-codegen" % "0.3.0"
// fs2-grpc
addSbtPlugin("org.lyranthe.fs2-grpc" % "sbt-java-gen" % "1.0.1")