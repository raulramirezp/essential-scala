ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.13"

val scalacheckVersion = "3.2.19"
val AkkaVersion = "2.9.3"
lazy val akkaHttpVersion = sys.props.getOrElse("akka-http.version", "10.6.3")
val commonDependencies = Seq(
  "org.scalactic" %% "scalactic" % "3.2.19",
  "org.scalatest" %% "scalatest" % scalacheckVersion % "test"
)

lazy val root = (project in file("."))
  .settings(
    name := "ScalaAndFP"
  )
  .aggregate(
    scala_with_cats_module,
    essential_scala_module,
    essential_effects,
    akka_fundamentals
  )

lazy val akka_fundamentals = (project in file("AkkaFundamentals"))
  .dependsOn(scala_with_cats_module)
  .settings(
    resolvers += "Akka library repository".at("https://repo.akka.io/maven"),
    libraryDependencies ++= Seq(
      "io.monix" %% "monix" % "3.4.0",
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % AkkaVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
      "ch.qos.logback" % "logback-classic" % "1.2.13"
    )
  )

lazy val essential_effects = (project in file("EssentialEffects"))
  .dependsOn(scala_with_cats_module)

lazy val scala_with_cats_module = (project in file("ScalaWithCats"))
  .settings(
    libraryDependencies ++= commonDependencies,
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.12.0",
    libraryDependencies += "org.typelevel" %% "cats-effect" % "2.5.1"
  )

lazy val essential_scala_module = (project in file("EssentialScala"))
  .settings(
    libraryDependencies ++= commonDependencies
  )
