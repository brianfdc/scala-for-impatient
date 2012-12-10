import sbt._
import Keys._

/**
 * @see http://www.scala-sbt.org/release/docs/Examples/Full-Configuration-Example.html
 * @see https://github.com/scalatra/scalatra/tree/develop/project
 * @see https://github.com/dispatch/dispatch/tree/master/project
 */
object BuildSettings {
  val buildProject      = "scala-learn"
  val buildOrganization = "org.awong"
  val buildVersion      = "0.0.1-SNAPSHOT"
  val buildScalaVersion = "2.9.2"

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := buildOrganization,
    version      := buildVersion,
    scalaVersion := buildScalaVersion,
    shellPrompt  := ShellPrompt.buildShellPrompt
  )
}

// Shell prompt which show the current project,
// git branch and build version
object ShellPrompt {
  object devnull extends ProcessLogger {
    def info (s: => String) {}
    def error (s: => String) { }
    def buffer[T] (f: => T): T = f
  }
  def currBranch = (
    ("git status -sb" lines_! devnull headOption)
      getOrElse "-" stripPrefix "## "
  )

  val buildShellPrompt = {
    (state: State) => {
      val currProject = Project.extract (state).currentProject.id
      "%s:%s:%s> ".format (
        currProject, currBranch, BuildSettings.buildVersion
      )
    }
  }
}

object Resolvers {
  val sunrepo    = "Sun Maven2 Repo" at "http://download.java.net/maven/2"
  val sunrepoGF  = "Sun GF Maven2 Repo" at "http://download.java.net/maven/glassfish"
  val oraclerepo = "Oracle Maven2 Repo" at "http://download.oracle.com/maven"
  
  val akkaRepo   = "Akka Repo" at "http://repo.akka.io/repository"

  val oracleResolvers = Seq (sunrepo, sunrepoGF, oraclerepo)
}

object Dependencies {
  val akkaOrg = "com.typesafe.akka"
  val akkaVer = "2.0.4"

  val junit = "junit" % "junit" % "4.4" % "test"

  val scalatest = "org.scalatest" %% "scalatest" % "1.8" % "test"

  val akkaActor       = akkaOrg % "akka-actor" % akkaVer
  val akkaRemote      = akkaOrg % "akka-remote" % akkaVer
  val akkaSlf4j       = akkaOrg % "akka-slf4j" % akkaVer
  val akkaTestkit     = akkaOrg % "akka-testkit" % akkaVer % "test"
  val akkaFileMailbox = akkaOrg % "akka-file-mailbox" % akkaVer
  val akkaKernel      = akkaOrg % "akka-kernel" % akkaVer
  val akkaZeroMQ      = akkaOrg % "akka-zeromq" % akkaVer
  val akkaTransactor  = akkaOrg % "akka-zeromq" % akkaVer

  val scalazFull = "org.scalaz" %% "scalaz-full" % "6.0.4"

  val guava = "com.google.guava" % "guava" % "12.0"

  val liftJson = "net.liftweb" % "lift-json_2.9.1" % "2.4-M4"

  val jodaTime = "joda-time" % "joda-time" % "1.6.2"
}

object ProjectBuild extends Build {
  import Resolvers._
  import Dependencies._
  import BuildSettings._

  // Sub-project specific dependencies
  lazy val all = Project (
    id = buildProject + "-all",
    base = file ("."),
    settings = buildSettings ++ Seq(
      description := "Wraps up all the modules"
    )
  ) aggregate (
    core,
    akka
  )

  lazy val core = Project (
    id = buildProject + "-core",
    base = file ("core"),
    settings = buildSettings ++ Seq (
      libraryDependencies ++= Seq(
        junit,
        guava,
        liftJson,
        jodaTime,
        scalatest
      ),
      description := "The core module"
    )
  )

  lazy val akka = Project (
    buildProject + "-akka",
    file ("akka"),
    settings = buildSettings ++ Seq (
      resolvers += akkaRepo,
      libraryDependencies ++= Seq(
        akkaActor,
        akkaRemote,
        akkaTestkit,
        scalatest
      ),
      description := "The akka module"
    )
  ) dependsOn (
    core % "compile;test->test;provided->provided"
  )

  
}