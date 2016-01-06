organization := "com.harrys.hyppo"

name := "hyppo-scala-test"

//  Bring in shared SBT configurations from project root
Common.buildSettings

//  Since this is not "ready" yet, all versions are snapshots
version := Common.snapshotVersion

libraryDependencies += "com.harrys.hyppo" % "source-api" % Common.hyppoVersion
