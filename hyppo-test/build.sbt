organization := "com.harrys.hyppo"

name := "hyppo-test"

//  Bring in shared SBT configurations from project root
Common.buildSettings

libraryDependencies += "com.harrys.hyppo" % "source-api" % Common.hyppoVersion