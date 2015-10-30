organization := "com.harrys.hyppo"

name := "source-api"

version := "0.5.0"

sonatypeProfileName := "com.harrys"

//  Don't export scala as a dependency (it's only for testing)
autoScalaLibrary := false

//  Don't inject scala version number into artifacts
crossPaths := false

libraryDependencies ++= Seq(
  "com.typesafe"    %  "config" % "1.3.0",      // Configuration API
  "org.apache.avro" %  "avro"   % "1.7.7"   // Record Serialization API
)

// --
//  Testing Setup
//--

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"

//  Setup the J-Unit arguments for testing
testOptions += Tests.Argument(TestFrameworks.JUnit, "-q")

// -- 
// Publishing to Sonatype OSS
// --

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

licenses := Seq("The MIT License (MIT)" -> url("http://opensource.org/licenses/MIT"))

homepage := Some(url("https://github.com/harrystech/hyppo-source-api"))

pomExtra := {
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
}


