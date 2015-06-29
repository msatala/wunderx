import sbt.Project.projectToRef

lazy val scalaVs = Seq("2.11.6")

lazy val wunderxServer = (project in file("wunderx-server")).settings(
  crossScalaVersions := scalaVs,
  scalaVersion := scalaVs.head,
  scalaJSProjects := Seq(wunderxClient),
  scalacOptions ++= Settings.scalacOptions,
  pipelineStages := Seq(scalaJSProd, gzip),
  libraryDependencies ++= Seq(
    "com.vmunier" %% "play-scalajs-scripts" % "0.1.0",
    "com.codemettle.jsactor" %% "jsactor-bridge-server" % "0.6.1"
  )).
  enablePlugins(PlayScala).
  aggregate(wunderxClient).
  dependsOn(wunderxSharedJvm)

lazy val wunderxClient = (project in file("wunderx-client")).settings(
  crossScalaVersions := scalaVs,
  scalaVersion := scalaVs.head,
  scalacOptions ++= Settings.scalacOptions,
  persistLauncher := true,
  persistLauncher in Test := false,
  sourceMapsDirectories += wunderxSharedJs.base / "..",
  unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value),
  libraryDependencies ++= Seq(
    "com.codemettle.jsactor" %%% "jsactor" % "0.6.1",
    "com.codemettle.jsactor" %%% "jsactor-bridge-client" % "0.6.1",
    "com.codemettle.jsactor" %%% "jsactor-loglevel" % "0.6.1",
    "org.scala-js" %%% "scalajs-dom" % "0.8.0",
    "biz.enef" %%% "scalajs-angulate" % "0.2.1",
    "com.lihaoyi" %%% "scalarx" % "0.2.8"
  ),
    skip in packageJSDependencies := false
    ).
  enablePlugins(ScalaJSPlugin, ScalaJSPlay).
  dependsOn(wunderxSharedJs)

lazy val wunderxShared = (crossProject.crossType(CrossType.Pure) in file("wunderx-shared")).
  settings(
    crossScalaVersions := scalaVs,
    scalaVersion := scalaVs.head,
    scalacOptions ++= Settings.scalacOptions,
    libraryDependencies ++= Seq(
      "com.codemettle.jsactor" %%% "jsactor-bridge-shared" % "0.6.1",
      "org.scala-js" %% "scalajs-stubs" % scalaJSVersion % "provided"
    )).
  jsConfigure(_ enablePlugins ScalaJSPlay).
  jsSettings(sourceMapsBase := baseDirectory.value / "..")

lazy val wunderxSharedJvm = wunderxShared.jvm
lazy val wunderxSharedJs = wunderxShared.js

// loads the jvm project at sbt startup
onLoad in Global := (Command.process("project wunderxServer", _: State)) compose (onLoad in Global).value
