organization := "com.spinoco"

name := "scalaz-presentation"

version := "snapshot-0.1"

scalaVersion := "2.10.2"

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
	"org.scalaz.stream" %% "scalaz-stream" % "0.3.1"
)
