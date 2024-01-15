ThisBuild / scalaVersion     := "2.13.12"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "testrepoconn",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.0.18",
      "dev.zio" %% "zio-test" % "2.0.18" % Test,
      "dev.zio" %% "zio-interop-cats" % "23.1.0.0",
      "org.tpolecat" %% "doobie-core" % "1.0.0-RC4",
      "org.tpolecat" %% "doobie-h2" % "1.0.0-RC4",
      "org.tpolecat" %% "doobie-hikari" % "1.0.0-RC4",
      "io.github.gaelrenoux" %% "tranzactio-doobie" % "5.0.1"
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
