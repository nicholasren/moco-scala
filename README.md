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
//create server and configure
val theServer = server(8080) {
    when(uri -> "/bar") then "foo"
}


//running the created server
running(theServer) {
    //testing your stuff
}
```