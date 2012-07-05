
name := "scala-for-impatient"

version := "0.0.1"

scalaVersion := "2.9.2"

resolvers += "Local Maven Repository" at "file://Users/awong/.m2/repository"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "junit" % "junit" % "4.4" % "test"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.8" % "test"

libraryDependencies += "com.typesafe.akka" % "akka-actor" % "2.0.2"

libraryDependencies += "com.typesafe.akka" % "akka-remote" % "2.0.2"

