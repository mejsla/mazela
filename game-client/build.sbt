name := """game-client"""

version := "1.0"

organization := "Mejsla"

lazy val root = (project in file("."))

scalaVersion := "2.12.2"
javaSource in Compile := baseDirectory.value / "foo"

resolvers ++=  Seq("jcenter" at "http://jcenter.bintray.com",
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases")

val monkeyVersion = "3.1.0-stable"

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "org.jmonkeyengine" % "jme3-core" % monkeyVersion,
  "org.jmonkeyengine" % "jme3-desktop" % monkeyVersion,
  "org.jmonkeyengine" % "jme3-jogl" % monkeyVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3"

//  "net.sourceforge.jtds" % "jtds" % "1.3.1",
//  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)
