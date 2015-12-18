lazy val root = project.in(file(".")).
  enablePlugins(ScalaJSPlugin)

name := "Scala.js apigee-sdk"

normalizedName := "apigee-scalajs-sdk"

version := "0.0.1-SNAPSHOT"

organization := "io.flexpbx"

scalaVersion := "2.11.7"

scalacOptions ++= Seq("-deprecation", "-feature", "-Xfatal-warnings")

jsDependencies +=
  "org.webjars" % "apigee-javascript-sdk" % "2.0.9" / "2.0.9/apigee.js"
  
resolvers += Resolver.mavenLocal

jsDependencies in Test += RuntimeDOM
