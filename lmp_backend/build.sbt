name := "lmp_backend"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-slick" % "0.6.1",
    "mysql" % "mysql-connector-java" % "5.1.32",
    "com.github.nscala-time" %% "nscala-time" % "1.4.0",
    "com.google.inject" % "guice" % "3.0",
    "javax.inject" % "javax.inject" % "1"
)     

play.Project.playScalaSettings
