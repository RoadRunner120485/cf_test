name := "aws-cf-iac-test"

version := "1.0"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq (
  "com.monsanto.arch" %% "cloud-formation-template-generator" % "3.7.0"
).map(_.force())
