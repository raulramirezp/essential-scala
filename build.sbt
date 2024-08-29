ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val scalacheckVersion = "3.2.19"
val AkkaVersion = "2.9.5"
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
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
      "ch.qos.logback" % "logback-classic" % "1.2.13"
    )
    //dependencyOverrides += "org.slf4j" % "slf4j-api" % "1.7.36"
  )

lazy val essential_effects = (project in file("EssentialEffects"))
  .dependsOn(scala_with_cats_module)

lazy val scala_with_cats_module = (project in file("ScalaWithCats"))
  .settings(
    libraryDependencies ++= commonDependencies,
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.12.0",
    libraryDependencies += "org.typelevel" %% "cats-effect" % "3.4.8"
  )

lazy val essential_scala_module = (project in file("EssentialScala"))
  .settings(
    libraryDependencies ++= commonDependencies
  )
