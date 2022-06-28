ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "sen-soor"
  )

val AkkaVersion = "2.6.19"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.2.12" % Test,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"
)
Test / parallelExecution := false
