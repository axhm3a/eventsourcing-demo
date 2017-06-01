name := "scala-event-sourcing"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.18",
  "com.typesafe.akka" %% "akka-http" % "10.0.6",
  "com.typesafe.akka" %% "akka-persistence" % "2.4.18",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.3",
  "org.iq80.leveldb" % "leveldb" % "0.7",
  "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",

  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.18" % "test"
)