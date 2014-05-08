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
  val buildScalaVersion = "2.10.2"

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization  := buildOrganization,
    version       := buildVersion,
    scalaVersion  := buildScalaVersion,
    scalacOptions ++= Seq("-unchecked", "-deprecation"),
    javacOptions  ++= Seq("-target", "1.6", "-source", "1.6"),
    shellPrompt  := ShellPrompt.buildShellPrompt,
    licenses := Seq("LGPL v3" -> url("http://www.gnu.org/licenses/lgpl.txt")),
    pomExtra := (
      <scm>
        <url>git@github.com:scala-for-impatient/reboot.git</url>
        <connection>scm:git:git@github.com:scala-for-impatient/reboot.git</connection>
      </scm>
      <developers>
        <developer>
          <id>awong</id>
          <name>Alan Wong</name>
          <url>http://www.github.com/alanktwong</url>
        </developer>
      </developers>)
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

  val springReleaseRepo           = "EBR Spring Release Repository" at "http://repository.springsource.com/maven/bundles/release"
  val springExternalReleaseRepo   = "EBR Spring External Release Repository" at "http://repository.springsource.com/maven/bundles/external"
  val springMilestoneRepo         = "Spring Milestone Repository" at "https://repo.springsource.org/libs-milestone"


  val jBossRepo = "JBoss Public Maven Repository Group" at "https://repository.jboss.org/nexus/content/groups/public-jboss/"

  val oracleResolvers = Seq (sunrepo, sunrepoGF, oraclerepo)
  val springAppResolvers = Seq(springReleaseRepo, springExternalReleaseRepo, jBossRepo, akkaRepo)
}

object Dependencies {
  val provided = "provided"
  val test = "test"
  val runtime = "runtime"

  val hibernateVer = "4.0.0.Final"

  val springOrg = "org.springframework"
  val springVer = "3.1.0.RELEASE"

  val akkaOrg = "com.typesafe.akka"
  val akkaVer = "2.0.4"

  val junit = "junit" % "junit" % "4.10" % test

  val scalatest = "org.scalatest" %% "scalatest" % "1.9.1" % "test"
  
  val akkaActor       = akkaOrg % "akka-actor" % akkaVer
  val akkaRemote      = akkaOrg % "akka-remote" % akkaVer
  val akkaSlf4j       = akkaOrg % "akka-slf4j" % akkaVer
  val akkaTestkit     = akkaOrg % "akka-testkit" % akkaVer % test
  val akkaFileMailbox = akkaOrg % "akka-file-mailbox" % akkaVer
  val akkaKernel      = akkaOrg % "akka-kernel" % akkaVer
  val akkaZeroMQ      = akkaOrg % "akka-zeromq" % akkaVer
  val akkaTransactor  = akkaOrg % "akka-zeromq" % akkaVer

  val scalazFull = "org.scalaz" %% "scalaz-full" % "6.0.4"

  val guava = "com.google.guava" % "guava" % "12.0"

  val liftJson = "net.liftweb" % "lift-json_2.9.1" % "2.4-M4"

  val jodaTime = "joda-time" % "joda-time" % "1.6.2"

  val xerces = "xerces" % "xercesImpl" % "2.9.1" % runtime
  
  val jettyContainer   = "org.mortbay.jetty" % "jetty" % "6.1.22" % "container"
  
  val springContext    = springOrg % "org.springframework.context" % springVer
  val springTxn        = springOrg % "org.springframework.transaction" % springVer
  val springOrm        = springOrg % "org.springframework.orm" % springVer
  val springWeb        = springOrg % "org.springframework.web" % springVer
  val springWebServlet = springOrg % "org.springframework.web.servlet" % springVer
  val springScala      = "org.springframework.scala" % "spring-scala" % "1.0.0.M1"

  val servletApi = "javax.servlet" % "javax.servlet-api" % "3.0.1" % provided
  val jspApi = "javax.servlet.jsp" % "javax.servlet.jsp-api" % "2.2.1" % provided
  val jstlApi = "javax.servlet.jsp.jstl" % "javax.servlet.jsp.jstl-api" % "1.2.1" % provided

  val commonsLogging = "commons-logging" % "commons-logging" % "1.1.1" % runtime

  val slf4j = "org.slf4j" % "slf4j-api" % "1.6.4" % runtime
  val slf4jlog4j = "org.slf4j" % "slf4j-log4j12" % "1.6.4" % runtime

  val hibernateValidator = "org.hibernate" % "hibernate-validator" % "4.2.0.Final"
  val hibernate = "org.hibernate" % "hibernate-core" % hibernateVer

  val javassist = "org.javassist" % "javassist" % "3.15.0-GA" % runtime

  val commonsDbcp = "commons-dbcp" % "commons-dbcp" % "1.4" % runtime

  val hsqldb = "org.hsqldb" % "hsqldb" % "2.2.6" % runtime
  val selenium = "org.seleniumhq.selenium" % "selenium-java" % "2.14.0" % test
}

object ProjectTasks {
   val hello = TaskKey[Unit]("hello", "Prints 'Hello World'")

   val helloTask = hello := {
     println("Hello World")
   }

}
object ProjectBuild extends Build {
  import Resolvers._
  import Dependencies._
  import BuildSettings._
  import ProjectTasks._

  // Sub-project specific dependencies
  lazy val all = Project (
    id = buildProject + "-all",
    base = file ("."),
    settings = buildSettings ++ Seq(
      helloTask,
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


  lazy val spring = Project (
    buildProject + "-spring-app",
    file ("spring-app"),
    settings = buildSettings ++ Seq (
      resolvers ++= springAppResolvers,
      libraryDependencies ++= Seq(
        xerces,
        springContext,
        springTxn,
        springOrm,
        springWeb,
        springWebServlet,
        springScala,
        jettyContainer,
        servletApi,
        jspApi,
        jstlApi,
        commonsLogging,
        slf4j,
        slf4jlog4j,
        hibernateValidator,
        hibernate,
        javassist,
        commonsDbcp,
        hsqldb,
        selenium,
        akkaActor,
        akkaRemote,
        akkaTestkit,
        scalatest
      ),
      description := "The Spring module"
    )
  ) dependsOn (
    core % "compile;test->test;provided->provided"
  )
}