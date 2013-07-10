# APIs
Moco mainly focuses on server configuration. There are only two kinds of API right now: **Request** and **Response**.

That means if we get the expected request and then return our response. Now, you can see a Moco reference in details.


## Request

### Content
If you want to response according to request content, Moco server can be configured as following:

```scala
when(content -> "foo") then "bar"
```


If request content is too large, you can put it in a file:

```scala
when(file -> "path/to/file") then "bar"

```

### URI
If request uri is your major focus, Moco server could be like this:

```scala
when(uri -> "/foo") then "bar"
```

### Query parameter
Sometimes, your request has parameters:

```scala
//WIP
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
//WIP
```

### Header
We will focus HTTP header at times:

```scala
//WIP
when (header -> "foo" -> "bar") then "blah"
```

### Combined matcher
You can combine multiple matcher with `and` or `or` method in the `when` clause

```scala
when(uri -> "/foo"
        and method -> "get"
) then "bar"

when(uri -> "/foo"
        or method -> "get"
) then "bar"
```

### Cookie(WIP)

### Form(WIP)

### XML(WIP)

### XPath(WIP)

### JSON Request(WIP)
Json is rising with RESTful style architecture. Just like XML, in the most case, only JSON structure is important, so `json` operator can be used.


### match(WIP)

## Response

### Content(WIP)

As you have seen in previous example, response with content is pretty easy.
if you want to return same response for all requests, you can use `default`

```scala
default "bar"
```

The same as request, you can response with a file if content is too large to put in a string.


```scala
default file("path/to/your/file")
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

### Sequence(WIP)

### Mount(WIP)
