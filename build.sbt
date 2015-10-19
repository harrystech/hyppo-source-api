organization := "com.harrys.hyppo"

name := "source-api"

//  Don't export scala as a dependency (it's only for testing)
autoScalaLibrary := false

//  Don't inject scala version number into artifacts
crossPaths := false

//  Shared access point to change the required avro version
val AvroVersion = "1.7.7"

libraryDependencies ++= Seq(
  "com.typesafe"    %  "config" % "1.3.0",      // Configuration API
  "org.apache.avro" %  "avro"   % AvroVersion   // Record Serialization API
)

// --
//  Testing Setup
//--

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"

//  Setup the J-Unit arguments for testing
testOptions += Tests.Argument(TestFrameworks.JUnit, "-q")
