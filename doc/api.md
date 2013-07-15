# APIs
Moco mainly focuses on server configuration. There are only two kinds of API right now: **Request** and **Response**.

That means if we get the expected request and then return our response. Now, you can see a Moco reference in details.


## Server
If you want to create a server listing on port `8080`:

```scala
val theServer = server(8080)
```

## Record Behavior
If you want to record behavior for the created server:

```scala
theServer record (
    when(uri -> "/foo") then "bar"
)
```

### multiple behaviors
you could supply multiple behaviors for your server

```scala
theServer record (
    when(uri -> "/foo") then "bar",
    when(uri -> "/hello") then "world"
)
```

## Request

### Content
If you want to response according to request content, Moco server can be configured as following:

```scala
when(content -> "foo") then "bar"
```


If request content is too large, you can put it in a file:

```scala
when(content -> file("path/to/your/file")) then "bar"

```

### URI
If request uri is your major focus, Moco server could be like this:

```scala
when(uri -> "/foo") then "bar"
```

### Query parameter
Sometimes, your request has parameters:

```scala
when(param("foo") -> "bar") then "bar"
```

### HTTP method
It's easy to response based on specified HTTP method:

```scala
when(method -> "get") then "bar"
```

Also for POST / PUT /DELETE method:

```scala
when(method -> "post") then "bar"
when(method -> "put") then "bar"
when(method -> "delete") then "bar"
```


### Version
We can return different response for different HTTP version:

```scala
when(version -> "HTTP/1.1") then "1.1",
```

### Header
We will focus HTTP header at times:

```scala
when(header("foo") -> "bar") then "blah"
```

### Combined matcher
You can combine multiple matcher with `and` or `or` method in the `when` clause

```scala
when(uri -> "/foo" and method -> "get") then "bar"

when(uri -> "/foo"or method -> "get") then "bar"
```

### Cookie(WIP)

### Form
In web development, form is often used to submit information to server side.
```scala
post(form("name") -> "foo") then "bar"
```
### XML(WIP)

### XPath(WIP)

### JSON Request(WIP)
Json is rising with RESTful style architecture. Just like XML, in the most case, only JSON structure is important, so `json` operator can be used.


## Response

### Content

As you have seen in previous example, response with content is pretty easy.
if you want to return same response for all requests, you can use `default`

```scala
default("bar")
```

The same as request, you can response with a file if content is too large to put in a string.


```scala
default(file("path/to/your/file"))
```

### Status Code(WIP)
Moco also supports HTTP status code response.


### Version(WIP)

By default, response HTTP version is supposed to request HTTP version, but you can set your own HTTP version:

### Header(WIP)

### Url(WIP)

### Redirect(WIP)

### Cookie(WIP)

### Latency(WIP)

### Sequence
Sometimes, we want to simulate a real-world operation which change server side resource. For example:
* First time you request a resource and "foo" is returned
* We update this resource
* Again request the same URL, updated content, e.g. "bar" is expected.

We can do that by
```scala
theServer record (
    when(uri -> "/foo") then seq("first", "second", "third")
)
```
### Mount(WIP)
