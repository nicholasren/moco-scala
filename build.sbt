ThisBuild / organization := "com.github.nicholasren"
ThisBuild / scalaVersion := "2.12.8"
ThisBuild / version := "0.3.1"

lazy val smoco = (project in file("."))
  .settings(
    name := "moco-scala",
    libraryDependencies += "com.github.dreamhead" % "moco-core" % "0.10.0",
    libraryDependencies += "org.apache.httpcomponents" % "fluent-hc" % "4.2.5" % Test,
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % Test,
    libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % Test
  )

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")