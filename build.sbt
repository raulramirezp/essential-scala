ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"


val scalacheckVersion     = "3.2.13"
val commonDependencies = Seq(
  "org.scalactic" %% "scalactic" % "3.2.13",
  "org.scalatest" %% "scalatest" % scalacheckVersion % "test"
)

lazy val root = (project in file("."))
  .settings(
    name := "ScalaAndFP"
  )
  .aggregate(
    scala_with_cats_module,
    essential_scala_module
  )

lazy val scala_with_cats_module = project in file("ScalaWithCats")

lazy val essential_scala_module = (project in file("EssentialScala"))
  .settings(
    libraryDependencies ++= commonDependencies
  )