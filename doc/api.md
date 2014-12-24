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
- [uri](#matcher-uri)
- [method](#matcher-method)
- [text](#matcher-text)
- [file](#matcher-file)
- [version](#matcher-version)
- [header](#matcher-header)
- [query](#matcher-query)
- [cookie](#matcher-cookie)

#####matcher-uri

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
#####matcher-text

  by value
```scala
when {
    text("foo")
}
```
  by regex
```scala
when {
    text matched "hello.+"
}
```

#####matcher-file
```scala
when {
    file("foo.req")
}
```

#####matcher-version
```scala
when {
    version("HTTP/1.0")
}
```

#####matcher-header

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
#####matcher-query

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

#####matcher-cookie

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


#### Response Apis:
- [text](#response-text)
- [file](#response-file)
- [version](#response-version)
- [header](#response-header)
- [cookie](#response-cookie)
- [status](#response-status)
- [version](#response-version)

#####response-text

```scala
then {
    text("foo")
}
```

#####response-file
```scala
then {
    file("foo.req")
}
```

#####response-header

```scala
then {
    headers("Content-Type" -> "json", "Accept" -> "html")
}
```

#####response-cookie

```scala
then {
    cookie("foo" -> "bar")
}
```

##### response-status

```scala
then {
    status 200
}
```
##### response-version

```scala
then {
    version("HTTP/1.0")
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
