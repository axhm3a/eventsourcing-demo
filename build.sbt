name := "scala-event-sourcing"

version := "1.0"

scalaVersion := "2.12.1"

//resolvers += Resolver.url("artifactory", url("https://dl.bintray.com/sbt/sbt-plugin-releases/com.eed3si9n/sbt-assembly/scala_2.10/sbt_0.13/0.14.4/ivys/"))(Resolver.ivyStylePatterns)
//addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.4")


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.18",
  "com.typesafe.akka" %% "akka-http" % "10.0.6",
  "com.typesafe.akka" %% "akka-persistence" % "2.4.18",
  "org.json4s" %% "json4s-native" % "3.5.2",
  //"org.json4s" %% "json4s-ext" % "3.5.2"
  "org.json4s" %% "json4s-jackson" % "3.5.2",

  "org.iq80.leveldb" % "leveldb" % "0.7",
  "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8"
)