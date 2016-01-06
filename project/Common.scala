import sbt._
import Keys._


object Common {
  final val hyppoVersion  = "0.6.4"

  /**
    * @return A "-SNAPSHOT" suffixed version of the current version, even if the current version is not
    *         a snapshot.
    */
  def snapshotVersion = {
    if (hyppoVersion.endsWith("-SNAPSHOT")){
      hyppoVersion
    } else {
      hyppoVersion + "-SNAPSHOT"
    }
  }

  final val buildSettings = Seq(
    crossPaths        := false,
    autoScalaLibrary  := false,
    version           := hyppoVersion,
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
}