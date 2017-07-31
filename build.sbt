import SonatypeKeys._

sonatypeSettings

name := "moco-scala"

version := "0.3"

scalaVersion := "2.12.3"

crossScalaVersions := Seq("2.11.4", "2.12.3")

libraryDependencies += "com.github.dreamhead" % "moco-core" % "0.10.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test"

libraryDependencies += "org.apache.httpcomponents" % "fluent-hc" % "4.2.5" % "test"

libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test"

publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

organization := "com.github.nicholasren"

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { x => false }

pomExtra := (
  <url>https://github.com/nicholasren/moco-scala</url>
  <licenses>
    <license>
      <name>MIT</name>
      <url>https://raw.github.com/nicholasren/moco-scala/master/MIT-LICENSE.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:nicholasren/moco-scala.git</url>
    <connection>scm:git:git@github.com:nicholasren/moco-scala.git</connection>
  </scm>
  <developers>
    <developer>
      <id>nicholasren</id>
      <name>Nicholas Ren</name>
      <url>http://nicholasren.github.io</url>
    </developer>
  </developers>
)
