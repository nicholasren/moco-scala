### Concept
There are three important concepts in this wrapper: __request matcher__, __response hander__, and __resource__.

__Request matcher:__ sits in the *when* clause, used to match against requests that server received, once request matched, moco server will respond.

__Response handler:__ sits in the *then* clause, used to define which should be responded to client once request matched.

__Resource:__  Any thing which can be matched against or any thing which can be send back to client could be considered as a resource.


#### Config Apis:
Current moco support two global configurations: [file root](https://github.com/dreamhead/moco/blob/master/moco-doc/global-settings.md#file-root) and [context](https://github.com/dreamhead/moco/blob/master/moco-doc/global-settings.md#context).


###### file root

```scala
server configs {
  fileRoot("src/test/resources")
}
```

###### context
```scala
server configs {
  context("/hello")
}
```

#### Matcher Apis:
#####Uri

match by uri
```scala
when {
    uri("/hello")
}
```
or match by regex
```scala
when {
    uri matched "/hello.+"
}
```

#####matcher-method
```scala
when {
    method("get")
}
```
#####Text

by value
```scala
when {
    text("foo")
}
```
or by regex
```scala
when {
    text matched "hello.+"
}
```

#####File
```scala
when {
    file("foo.req")
}
```

#####Version
```scala
when {
    version("HTTP/1.0")
}
```

#####Header

match by value
```scala
when {
    header("Content-Type") === "application/json"
}
```
or by regex
```scala
when {
    header("Content-Type") matched ".+json"
}
```
#####Query

match by value
```scala
when {
     query("foo") === "bar"
}
```
or by regex:
```scala
when {
     query("foo") matched ".+bar"
}
```

#####Cookie

match by value
```scala
when {
    cookie("foo") === "bar"
}
```
  or by regex:
```scala
when {
    cookie("foo") matched ".+bar"
}
```

#####Form

you can do exact match by form value
```scala
when {
  form("foo") === "bar"
}
````
or by match value with regex

```scala
when {
  form("foo") matched "ba.+"
}
````

#####Xml

```scala
when {
  xml("<body>something</body>")
}
```

#####Xpath
similarly, you can do exact match by value
```scala
when {
  xpath("/request/parameters/id/text()") === "foo"
}

```
or match by regex

```scala
when {
  xpath("/request/parameters/id/text()") matched "fo.+"
}

```

#####Json
```scala
when {
  json("{\"foo\": \"bar\"}")
}
```
#####Jsonpath
similar to xpath.

#### Response Apis:

#####Text

```scala
then {
    text("foo")
}
```

#####File
```scala
then {
    file("foo.req")
}
```

#####Header

```scala
then {
    headers("Content-Type" -> "json", "Accept" -> "html")
}
```

#####Cookie

```scala
then {
    cookie("foo" -> "bar")
}
```

##### Status

```scala
then {
    status 200
}
```
##### Version

```scala
then {
    version("HTTP/1.0")
}
```

#####Proxy Apis


#####Single URL
We can response with a specified url, just like a proxy.

```scala
then {
  proxy("http://example.com")
}
```
#####Failover
Proxy also support failover

```scala
then {
  proxy("http://example.com") {
    failover("failover.json")
  }
}
```

#####Playback
We also supports playback with save remote request and resonse into local file.
```scala
then {
  proxy("http://example.com") {
    playback("playback.json")
  }
}
```

#####Batch URLs
Proxy also support proxying a batch of URLs in the same context
```scala
when {
  method("GET") and uri matched "/proxy/.*"
} then {
  proxy {
    from("/proxy") to ("http://localhost:9090/target")
  }
}
```

#####Redirect Api:

You can simply redirect a request to a different location:

```scala
when {
  uri("/redirect")
} then {
  redirectTo("/target")
}
```

#####Attachment
You can setup a attachment as response
```scala
then {
  attachment("filename", file("filepath"))
}
```

#####Latency
You can simulate a slow response:
```scala
then {
  //you need to import scala.concurrent.duration.Duration to have this syntax sugar
  latency(2 seconds)
}
```

#####Sequence
You can simulate a sequence of response:
```scala
then {
  seq("foo", "bar", "blah")
}
```


#####Event
You can specify a subsequent action once the response was sent:
```scala
server on {
    complete{
      get("http"//another_site)
    }
}
```


#####Asynchronous
You can use async api to fire event asynchronsously

```scala
server on {
    complete {
      async {
        get("http"//another_site)
      }
    }
}
```

### Advanced Usage

to enable advanced usage, you have to import conversions as follow:

```scala
import com.github.nicholasren.moco.dsl.Conversions._
```

#### Multiple matchers

```scala
when {
  uri("/hello") and method("post")
} then {
  text("world")
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

#### Multiple behaviours

```scala
when {
  method("get")
} then {
  text("get")
} when {
  method("post")
} then {
  text("post")
}
```
