moco-scala
==========

This is the scala wrapper for [moco](https://github.com/dreamhead/moco)


###How to use

* add dependency:

	libraryDependencies += "com.github.nicholasren" % "moco-scala" % "0.0.1"

* code sample:

```scala
val server = httpserver(port)

test("should return expected response") {
	server response "foo"
		running(server) {
    	val response = get(root)
	    assert(response === "foo")
    }
}
```