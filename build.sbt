name := """statsd-smoke-example"""

version := "1.0"

scalaVersion := "2.11.7"

resolvers += "mDialog releases" at "http://mdialog.github.com/releases/"

// Change this to another test framework if you prefer
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "io.prometheus" % "simpleclient" % "0.0.19",
  "io.prometheus" % "simpleclient_servlet" % "0.0.6",
  "org.eclipse.jetty" % "jetty-servlet" % "8.1.7.v20120910",
  "com.mdialog" %% "smoke" % "2.1.5"
)
// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"

