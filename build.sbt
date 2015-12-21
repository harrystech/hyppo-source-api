import sbt._
import Keys._

organization := "com.harrys.hyppo"

sonatypeProfileName := "com.harrys"

lazy val `source-api`  = project.in(file("source-api"))

lazy val `rest-client` = project.in(file("rest-client"))
  .dependsOn(`source-api`)

lazy val root = project.in(file("."))
  .aggregate(`source-api`, `rest-client`)
  .settings(
    publishArtifact := false
  )


