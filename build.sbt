import sbt._
import Keys._

organization := "com.harrys.hyppo"

sonatypeProfileName := "com.harrys"

lazy val `source-api`  = project.in(file("source-api"))

lazy val `rest-client` = project.in(file("rest-client"))
  .dependsOn(`source-api`)

lazy val `hyppo-test`  = project.in(file("hyppo-test"))
  .dependsOn(`source-api`)

lazy val `hyppo-api` = project.in(file("."))
  .aggregate(`source-api`, `rest-client`, `hyppo-test`)
  .settings(
    publishArtifact := false
  )


