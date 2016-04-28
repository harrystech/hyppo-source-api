import sbt._
import Keys._

organization := "com.harrys.hyppo"

sonatypeProfileName := "com.harrys"

lazy val commonSettings = Seq(
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint", "-Werror"),
  javacOptions in (Compile, doc) := DefaultOptions.javac,
  crossPaths        := false,
  autoScalaLibrary  := false,
  version           := "1.0.0-SNAPSHOT",
  licenses          := Seq("The MIT License (MIT)" -> url("http://opensource.org/licenses/MIT")),
  homepage          := Some(url("https://github.com/harrystech/hyppo-source-api")),
  pomExtra          := {
    <scm>
      <url>git@github.com:harrystech/hyppo-source-api.git</url>
      <connection>scm:git:git@github.com:harrystech/hyppo-source-api.git</connection>
    </scm>
      <developers>
        <developer>
          <id>pettyjamesm</id>
          <name>James Petty</name>
        </developer>
      </developers>
  },
  publishMavenStyle       := true,
  publishArtifact in Test := false,
  pomIncludeRepository    := { _ => false }
)


lazy val `source-api`  = project.in(file("source-api"))
  .settings(commonSettings)

lazy val `rest-client` = project.in(file("rest-client"))
  .settings(commonSettings)
  .dependsOn(`source-api`)

lazy val `hyppo-test`  = project.in(file("hyppo-test"))
  .settings(commonSettings)
  .settings(
      //  This library is not ready for anything other than snapshot releases
      version := { if (version.value.endsWith("-SNAPSHOT")) version.value else version.value + "-SNAPSHOT" }
  )
  .dependsOn(`source-api`)

lazy val `hyppo-api` = project.in(file("."))
  .settings(commonSettings)
  .settings(
    publishArtifact := false
  )
  .aggregate(`source-api`, `rest-client`, `hyppo-test`)


