
organization := "com.harrys.hyppo"

name := "source-api"

libraryDependencies ++= Seq(
  "com.typesafe"    %  "config" % "1.3.0",  // Configuration API
  "org.apache.avro" %  "avro"   % "1.8.0"   // Record Serialization API
)

// --
//  Testing Setup
//--

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test

//  Setup the J-Unit arguments for testing
testOptions += Tests.Argument(TestFrameworks.JUnit, "-q")

