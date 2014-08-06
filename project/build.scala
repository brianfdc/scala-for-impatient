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
  val buildVersion      = "0.0.2-SNAPSHOT"
  val buildScalaVersion = "2.10.1"
  val javacVersion      = "1.7"

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization  := buildOrganization,
    version       := buildVersion,
    scalaVersion  := buildScalaVersion,
    scalacOptions ++= Seq("-unchecked", "-deprecation"),
    javacOptions  ++= Seq("-target", javacVersion, "-source", javacVersion),
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
  lazy val sunRepo    = "Sun Maven2 Repo" at "http://download.java.net/maven/2"
  lazy val sunRepoGF  = "Sun GF Maven2 Repo" at "http://download.java.net/maven/glassfish"
  lazy val oracleRepo = "Oracle Maven2 Repo" at "http://download.oracle.com/maven"
  lazy val oracleResolvers = Seq (sunRepo, sunRepoGF, oracleRepo)
  
  lazy val typesafeReleases      = "Typesafe Releases Repository"  at "http://repo.typesafe.com/typesafe/releases/"
  lazy val typesafeSnapshots     = "Typesafe Snapshots Repository" at "http://repo.typesafe.com/typesafe/snapshots/"
  lazy val typesafeResolvers     = Seq(typesafeReleases, typesafeSnapshots)

  lazy val springReleaseRepo           = "EBR Spring Release Repository" at "http://repository.springsource.com/maven/bundles/release"
  lazy val springExternalReleaseRepo   = "EBR Spring External Release Repository" at "http://repository.springsource.com/maven/bundles/external"
  lazy val springMilestoneRepo         = "Spring Milestone Repository" at "https://repo.springsource.org/libs-milestone"
  lazy val springAppResolvers = Seq(springReleaseRepo, springExternalReleaseRepo)
  
  lazy val jBossRepo = "JBoss Public Maven Repository Group" at "https://repository.jboss.org/nexus/content/groups/public-jboss/"

  lazy val coreResolvers = typesafeResolvers ++ oracleResolvers
  lazy val akkaResolvers = coreResolvers
}

object Versions {
  lazy val javacVersion = BuildSettings.javacVersion
  lazy val scalaVersion = BuildSettings.buildScalaVersion
  
  lazy val scalaTestVer = "2.0"
  lazy val junitVer     = "4.11"

  lazy val slf4jVer     = "1.6.4"
  lazy val logbackVer   = "1.0.7"
  lazy val configVer    = "1.0.2"  // typesafe config
  lazy val scalazVer    = "7.1.0"

  lazy val hibernateVer = "4.0.0.FINAL"
  lazy val springVer    = "3.1.0.RELEASE"
  lazy val akkaVer      = "2.2.0"
}


object Dependencies {
  import Versions._
  val provided = "provided"
  val test     = "test"
  val runtime  = "runtime"

  lazy val seleniumOrg = "org.seleniumhq.selenium"
  lazy val junit       = "junit"           %  "junit"         % junitVer     % test
  lazy val scalaTest   = "org.scalatest"   %% "scalatest"     % scalaTestVer % test
  lazy val specs2      = "org.specs2"      %% "specs2"        % "2.4"        % test
  lazy val selenium    = seleniumOrg       %  "selenium-java" % "2.14.0"     % test
  lazy val scalaCheck  = "org.scalacheck"  %% "scalacheck"    % "1.11.3"     % test
  
  lazy val testDependencies = Seq(junit, scalaTest, specs2)
  
  lazy val typesafeOrg     = "com.typesafe"
  lazy val akkaOrg         = typesafeOrg + ".akka"
  lazy val akkaActor       = akkaOrg      %% "akka-actor"      % akkaVer
  lazy val akkaAgent       = akkaOrg      %% "akka-agent"      % akkaVer
  lazy val akkaCamel       = akkaOrg      %% "akka-agent"      % akkaVer
  lazy val akkaCluster     = akkaOrg      %% "akka-cluster"    % akkaVer
  lazy val akkaKernel      = akkaOrg      %% "akka-kernel"     % akkaVer
  lazy val akkaOSGI        = akkaOrg      %% "akka-osgi"       % akkaVer
  lazy val akkaOSGI_aries  = akkaOrg      %% "akka-osgi-aries" % akkaVer
  lazy val akkaRemote      = akkaOrg      %% "akka-remote"     % akkaVer
  lazy val akkaSlf4j       = akkaOrg      %% "akka-slf4j"      % akkaVer
  lazy val akkaTestkit     = akkaOrg      %% "akka-testkit"    % akkaVer % test
  lazy val akkaZeroMQ      = akkaOrg      %% "akka-zeromq"     % akkaVer
  
  lazy val typesafeConfig  = typesafeOrg        %% "config"       % configVer
  lazy val scalazFull      = "org.scalaz"       %% "scalaz-full"  % scalazVer
  lazy val guava           = "com.google.guava" %  "guava"        % "12.0"
  lazy val jodaTime        = "joda-time"        %  "joda-time"    % "1.6.2"
  lazy val xerces          = "xerces"           %  "xercesImpl"   % "2.9.1" % runtime
  lazy val liftJson        = "net.liftweb"      %% "lift-json"    % "2.5"
  
  lazy val springOrg        = "org.springframework"
  lazy val springScalaOrg   = springOrg + ".scala"
  lazy val springContext    = springOrg      % "org.springframework.context"     % springVer
  lazy val springTxn        = springOrg      % "org.springframework.transaction" % springVer
  lazy val springOrm        = springOrg      % "org.springframework.orm"         % springVer
  lazy val springWeb        = springOrg      % "org.springframework.web"         % springVer
  lazy val springWebServlet = springOrg      % "org.springframework.web.servlet" % springVer
  lazy val springScala      = springScalaOrg % "spring-scala"                    % "1.0.0.M1"

  lazy val servletApi = "javax.servlet"          % "javax.servlet-api"          % "3.0.1" % provided
  lazy val jspApi     = "javax.servlet.jsp"      % "javax.servlet.jsp-api"      % "2.2.1" % provided
  lazy val jstlApi    = "javax.servlet.jsp.jstl" % "javax.servlet.jsp.jstl-api" % "1.2.1" % provided

  lazy val commonsLogging = "commons-logging" % "commons-logging" % "1.1.1" % runtime
  lazy val commonsDbcp    = "commons-dbcp"    % "commons-dbcp"    % "1.4"

  lazy val slf4j_api      = "org.slf4j"      % "slf4j-api"       % slf4jVer
  lazy val slf4j_simple   = "org.slf4j"      % "slf4j-simple"    % slf4jVer
  lazy val slf4j_log4j    = "org.slf4j"      % "slf4j-log4j12"   % slf4jVer
  lazy val logbackClassic = "ch.qos.logback" % "logback-classic" % logbackVer
  lazy val logbackCore    = "ch.qos.logback" % "logback-core"    % logbackVer
  lazy val logbackAccess  = "ch.qos.logback" % "logback-access"  % logbackVer
  
  lazy val slf4jDependencies = Seq(slf4j_api, logbackClassic)

  lazy val hibernateValidator = "org.hibernate" % "hibernate-validator" % "4.2.0.Final"
  lazy val hibernate          = "org.hibernate" % "hibernate-core"      % hibernateVer
  lazy val javassist          = "org.javassist" % "javassist"           % "3.15.0-GA" % runtime
  lazy val hsqldb             = "org.hsqldb"    % "hsqldb"              % "2.2.6"     % runtime

  lazy val coreDependencies   = testDependencies ++ Seq(guava, jodaTime, liftJson) ++ slf4jDependencies
  lazy val akkaDependencies   = coreDependencies ++ Seq(akkaActor, akkaCluster, akkaRemote, akkaTestkit)
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
      resolvers ++= coreResolvers,
      libraryDependencies ++= coreDependencies,
      description := "The core module"
    )
  )

  lazy val akka = Project (
    buildProject + "-akka",
    file ("akka"),
    settings = buildSettings ++ Seq (
      resolvers ++= akkaResolvers,
      libraryDependencies ++= akkaDependencies,
      description := "The akka module"
    )
  ) dependsOn (
    core % "compile;test->test;provided->provided"
  )

}