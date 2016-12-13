
organization := "com.harrys.hyppo"

name := "rest-client"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.0",
  "commons-io" % "commons-io" % "2.4",
  "org.slf4j" % "slf4j-api" % "1.7.13",
  "org.apache.httpcomponents" % "httpclient" % "4.5.1",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.2",
  //  The exclusion of jackson here is necessary to standardize on the jackson 2 annotations
  "com.harrys.hyppo" % "source-api" % version.value excludeAll ExclusionRule(organization = "org.codehaus.jackson")
)
