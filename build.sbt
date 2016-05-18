organization := "com.github.strattonbrazil.checklist"

name := "Checklist"

version := "1.0-SNAPSHOT"

// libraryDependencies ++= Seq( <any normal jar deps> )


libraryDependencies ++= Seq(
  "junit" % "junit" % "4.11" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test
        exclude("junit", "junit-dep")
)
