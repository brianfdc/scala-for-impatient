name := "scalaz-learn"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.10.4"


libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.0.6",
  "org.scalaz" %% "scalaz-effect" % "7.0.6",
  "org.scalaz" %% "scalaz-typelevel" % "7.0.6",
  "org.scalaz" %% "scalaz-scalacheck-binding" % "7.0.6" % "test"
)

scalacOptions += "-feature"

initialCommands in console := "import scalaz._, Scalaz._"

