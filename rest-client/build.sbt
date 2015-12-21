
organization := "com.harrys.hyppo"

name := "hyppo-rest-client"

//  Bring in shared SBT configurations from project root
Common.buildSettings

crossPaths        := false

autoScalaLibrary  := false

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.0",
  "commons-io" % "commons-io" % "2.4",
  "org.slf4j" % "slf4j-api" % "1.7.13",
  "org.apache.httpcomponents" % "httpclient" % "4.5.1",
  "com.harrys.hyppo" % "source-api" % Common.hyppoVersion
)
