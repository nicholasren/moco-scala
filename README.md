moco-scala
==========

This is a scala wrapper for [moco](https://github.com/dreamhead/moco).


### How to use

Add dependency:
```sbt
libraryDependencies += "com.github.nicholasren" % "moco-scala" % "0.1-SNAPSHOT"
```

### Sample:

#### import dependencies
```scala
import org.github.nicholasren.moco.scala.dsl.SMoco._
```

#### create server
```scala
val theServer = server(8080)
```

#### record behaviour
```scala
theServer record {
    when(uri -> "/bar") then "foo"
}
```

#### running server and test your stuff

```scala
running(theServer) {
    //testing your stuff
}
```

for detail api, please find in the [API reference](doc/api.md)