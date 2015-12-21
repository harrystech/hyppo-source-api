
organization := "com.harrys.hyppo"

name := "source-api"

//  Bring in shared SBT configurations from project root
Common.buildSettings

crossPaths        := false

autoScalaLibrary  := false

libraryDependencies ++= Seq(
  "com.typesafe"    %  "config" % "1.3.0",  // Configuration API
  "org.apache.avro" %  "avro"   % "1.7.7"   // Record Serialization API
)

// --
//  Testing Setup
//--

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"

//  Setup the J-Unit arguments for testing
testOptions += Tests.Argument(TestFrameworks.JUnit, "-q")

