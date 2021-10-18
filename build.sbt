val scala3Version = "3.0.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "gaze",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0-M1" % Test
  )
