organization := "com.github.strattonbrazil.checklist"

name := "Checklist"

version := "1.0-SNAPSHOT"

mainClass in Compile := Some("com.github.strattonbrazil.checklist.App")

autoScalaLibrary := false

// libraryDependencies ++= Seq( <any normal jar deps> )
libraryDependencies += "org.codehaus.groovy" % "groovy-all" % "2.4.6"

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.11" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test
        exclude("junit", "junit-dep")
)

// http://mvnrepository.com/artifact/org.scala-lang/scala-compiler
//libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.11.8"


// http://mvnrepository.com/artifact/org.scala-lang/scala-library
//libraryDependencies += "org.scala-lang" % "scala-library" % "2.11.8"

// http://mvnrepository.com/artifact/org.python/jython
libraryDependencies += "org.python" % "jython" % "2.7.0"
