organization := "com.spinoco"

name := "scalaz-presentation"

version := "snapshot-0.1"

scalaVersion := "2.10.2"

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
	//"org.scalaz.stearm" %% "scalaz-stream" % "snapshot-0.4"
	"org.scalaz.stream" %% "scalaz-stream" % "0.3.1"
  //"spinoco" %% "scalaz-stream" % "0.1.0.127-SNAPSHOT" //exclude("org.scala-lang", "*")
  /*
  "spinoco" %% "scalaz-stream" % "0.1.0.127-SNAPSHOT" exclude("org.scala-lang", "*"),
  "org.scalaz" %% "scalaz-core" % "7.0.4" exclude("org.scala-lang","*"),
  "org.scalaz" %% "scalaz-concurrent" % "7.0.4" exclude("org.scala-lang","*")
  */
)
