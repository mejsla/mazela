name := """game-client"""

version := "1.0"

organization := "Mejsla"

lazy val root = (project in file("."))

scalaVersion := "2.12.2"
//javaSource in Compile := baseDirectory.value / "foo"

resolvers ++=  Seq("jcenter" at "http://jcenter.bintray.com",
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  Resolver.mavenLocal)

val monkeyVersion = "3.1.0-stable"

libraryDependencies ++= Seq(
  "com.google.guava" % "guava" % "23.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "org.jmonkeyengine" % "jme3-core" % monkeyVersion,
  "org.jmonkeyengine" % "jme3-desktop" % monkeyVersion,
  "org.jmonkeyengine" % "jme3-jogl" % monkeyVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "se.mejsla.camp.mazela" % "mazela-network-client"  % "1.0-SNAPSHOT",
  "default" % "scala-network_2.12" % "0.1-SNAPSHOT"
)
