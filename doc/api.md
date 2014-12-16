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

#####uri [matcher-uri]

match by uri
```
when {
    uri("/hello")
}```
or match by regex
```
when {
    uri matched "/hello.+"
}```

#####method [matcher-method]
```
when {
    method("get")
}```
#####text[matcher-text]

  by value
```
when {
    text("foo")
}```
  by regex
```
when {
    text matched "hello.+"
}```

#####file[matcher-file]
```
when {
    file("foo.req")
}```

#####version[matcher-version]
```
when {
    version("HTTP/1.0")
}```
#####header[matcher-header]

match by value
```
when {
    header("Content-Type") === "application/json"
}```
or by regex
```
when {
    header("Content-Type") matched ".+json"
}```
#####query[matcher-query]

match by value
```
when {
     query("foo") === "bar"
}```
   or by regex:
```
when {
     query("foo") matched ".+bar"
}```

#####cookie[matcher-cookie]

  match by value
```
when {
    cookie("foo") === "bar"
}```
  or by regex:
```
when {
    cookie("foo") matched ".+bar"
}```


#### Response Apis:
- [text](#response-text)
- [file](#response-file)
- [version](#response-version)
- [header](#response-header)
- [cookie](#response-cookie)
- [status](#response-status)
- [version](#response-version)

#####text[response-text]

```
then {
    text("foo")
}```

#####file[response-file]
```
then {
    file("foo.req")
}```

#####header[response-header]

```
then {
    headers("Content-Type" -> "json", "Accept" -> "html")
}```

#####cookie[response-cookie]

```
then {
    cookie("foo" -> "bar")
}```

#####status[response-status]

```
then {
    status 200
}```
#####versionresponse-version]

```
then {
    version("HTTP/1.0")
}```


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
