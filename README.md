Hi. If you are reading this, you are brave, brave person. These contain my answers to _Scala for the Impatient_, as well as other attempts to play around with Scala code such as [Akka](http://akka.io/) 2.0.4.

My environment is of [Scala](http://www.scala-lang.org/) 2.9.2, [Scala IDE](http://scala-ide.org/) 2.0.2 plugin for Eclipse 3.7 (Indigo) and [sbt](http://www.scala-sbt.org/) 0.12.1 installed.

- If you're using the Scala IDE plugin for eclipse, set up the sbt project and then use the [sbteclipse](https://github.com/typesafehub/sbteclipse) plugin to generate the Eclipse projects you need so that they can be imported into your workspace.
- If you're use IntelliJ, set up the sbt project and then use the [sbt-idea](https://github.com/mpeltonen/sbt-idea) plugin to generate the your IntelliJ project.


The modules for this repository are:

1. `core`: focuses on the Scala language
2. `akka`: focus on the Akka library

Experiment with FSMs

- Akka Style http://doc.akka.io/docs/akka/2.0.3/scala/fsm.html
- Then Uncle Bob's Turnstyle http://www.objectmentor.com/resources/articles/umlfsm.pdf