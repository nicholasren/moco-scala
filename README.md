moco-scala
==========

This is a scala wrapper for [moco](https://github.com/dreamhead/moco).


### How to use

__Add dependency:__
```sbt
libraryDependencies += "com.github.nicholasren" % "moco-scala_2.10" % "0.2"
```
__Try latest version:__

if you want to try latest version, just add snapshot repo to dependency resolver

```scala
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
```

###Quick Start:

#### import dependencies
```scala
import com.github.nicholasren.moco.scala.dsl.SMoco._
```

#### Create server
```scala
val theServer = server(8080)
```

#### Record behaviour
```scala
theServer record {
    when {
        uri("/hello")
    } then {
        status(200)
    }
}
```

#### Running server and test your stuff

```scala
theServer running  {
    assert(getForStatus(remoteUrl("/hello")) === 200)
}
```


### Concept:
There are three important concepts in this wrapper: __request matcher__, __response hander__, and __resource__.

__Request matcher:__ sits in the *when* clause, used to match against requests that server received, once request matched, moco server will respond.

__Response handler:__ sits in the *then* clause, used to define which should be responded to client once request matched.

__Resource:__  Any thing which can be match against or any thing which could be send back to client could be considered as a resource. the availabe resources are:

+ uri
+ method
+ text
+ file

### Advanced Usage

to enable advanced usage, you have to import conversions as follow:

```scala
import com.github.nicholasren.moco.dsl.Conversions._
```

#### Multiple matchers

```scala
theServer record {
  when {
    uri("/hello") and method("post")
  } then {
    text("world")
  }
}

```
#### Multiple responses
```scala
when {
  uri("/not-exits")
} then {
  status(400) and text("BAD REQUEST")
}
```

#### Multiple requests

```scala
theServer record {
  List(
    when {
      method("get")
    } then {
      text("get")
    },
    when {
      method("post")
    } then {
      text("post")
    }
  )
}
```
for more usage, please refer to [tests](https://github.com/nicholasren/moco-scala/blob/master/src/test/scala/com/github/nicholasren/moco/dsl/SMocoTest.scala) or refer to [moco's api doc](https://github.com/dreamhead/moco/blob/master/moco-doc/apis.md).

### Contribution:
This project is still in process, any question and suggestion are more than welcomed.
