organization := "com.github.strattonbrazil.checklist"

name := "Checklist"

version := "1.0-SNAPSHOT"

mainClass in Compile := Some("com.github.strattonbrazil.checklist.App")

//autoScalaLibrary := false

// libraryDependencies ++= Seq( <any normal jar deps> )
libraryDependencies += "org.codehaus.groovy" % "groovy-all" % "2.4.6"

// http://mvnrepository.com/artifact/commons-cli/commons-cli
libraryDependencies += "commons-cli" % "commons-cli" % "1.3.1"

// http://mvnrepository.com/artifact/io.reactivex/rxjava
libraryDependencies += "io.reactivex" % "rxjava" % "1.1.5"

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.11" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test
        exclude("junit", "junit-dep")
)
