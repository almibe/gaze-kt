val scala3Version = "3.1.0"

version          := "0.1.0-SNAPSHOT"
organization     := "dev.ligature"
organizationName := "ligature"
name             := "gaze"

lazy val root = project
  .in(file("."))
  .settings(
    scalaVersion := scala3Version,
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0-M1" % Test
  )
