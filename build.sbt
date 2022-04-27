import Wart._

val scala2Version = "2.13.8"

lazy val root = project
  .in(file("."))
  .settings(
    name := "global--",
    version := "0.1.0-SNAPSHOT",
    organization := "dev.fosfor",

    scalaVersion := scala2Version,
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-unchecked",
      "-language:higherKinds",
      "-Ywarn-value-discard",
    ),
    scalacOptions ++= {
      if (insideCI.value) Seq("-Wconf:any:error")
      else                Seq("-Wconf:any:warning")
    },
    wartremoverWarnings ++= Warts.allBut(
      Recursion, Throw, Nothing, Return, While, IsInstanceOf,
      Var, MutableDataStructures, NonUnitStatements,
      DefaultArguments, ImplicitParameter, ImplicitConversion,
      StringPlusAny, Any,
      JavaSerializable, Serializable, Product,
      LeakingSealed, Overloading,
      Option2Iterable, TraversableOps, ListAppend
    ),

    libraryDependencies += "com.lihaoyi" %% "fastparse" % "2.3.2",
    libraryDependencies += "com.lihaoyi" %% "ammonite-ops" % "2.4.0",
  )
