moco-scala
==========

This is the scala wrapper for [moco](https://github.com/dreamhead/moco).


### How to use

Add dependency:
```sbt
libraryDependencies += "com.github.nicholasren" % "moco-scala" % "0.0.1"
```

Sample:
```scala
//create server
val server = httpserver(8080)

//configure
server request(by("foo")) response("bar")

//using
running(server) {
//do your stuff
}
```