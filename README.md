moco-scala
==========

This is a scala wrapper for [moco](https://github.com/dreamhead/moco).



### How to use

Add dependency:
```sbt
libraryDependencies += "com.github.nicholasren" % "moco-scala_2.10" % "0.1-SNAPSHOT"
```
### Try latest version
if you want to try latest version, just add snapshot repo to dependency resolver

```scala
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
```

### Example:

#### import dependencies
```scala
import com.github.nicholasren.moco.scala.dsl.SMoco._
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