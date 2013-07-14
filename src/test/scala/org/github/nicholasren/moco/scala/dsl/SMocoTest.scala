package org.github.nicholasren.moco.scala.dsl

import org.scalatest.{BeforeAndAfter, FunSuite}

import SMoco._
import org.github.nicholasren.moco.scala.helper.RemoteTestHelper._
import RequestMatcherBuilder._
import com.google.common.io.ByteStreams

class SMocoTest extends FunSuite with BeforeAndAfter {

  var theServer: SMoco = null

  before {
    theServer = server(8080)
  }

  test("should return expected response") {
    theServer record (default("bar"))

    running(theServer) {
      assert(get(root) === "bar")
    }
  }

  test("should return expected response based on specified request uri") {
    theServer record (
      when(uri -> "/foo") then "bar",
      when(uri -> "/hello") then "world"
    )

    running(theServer) {
      assert(get(remoteUrl("/foo")) === "bar")
      assert(get(remoteUrl("/hello")) === "world")
    }
  }

  test("should return expected response based on specified request method") {
    theServer record (
      when(method -> "GET") then "foo"
    )

    running(theServer) {
      assert(get(root) === "foo")
      assert(statusCode("POST", root) === 400)
    }
  }

  test("should return expected response based on path resource") {
    theServer record (
      when(pathResource -> "foo.request") then "bar"
    )

    running(theServer) {
      val asStream = this.getClass().getClassLoader().getResourceAsStream("foo.request");

      assert(postBytes(root, ByteStreams.toByteArray(asStream)) === "bar")
    }
  }

  test("should return expected response from file") {
    theServer record (
      default(file("src/test/resources/bar.response"))
    )

    running(theServer) {
      assert(get(root) === "bar")
    }
  }

  test("should match request based on multiple matchers with AND operator") {
    theServer record (
      when(uri -> "/foo" and method -> "GET")
        then "bar"
    )

    running(theServer) {
      assert(get(remoteUrl("/foo")) === "bar")
      assert(statusCode("POST", root) === 400)
    }
  }

  test("should match request based on multiple matchers with OR operator") {
    theServer record (
      when(uri -> "/foo" or method -> "GET") then "bar"
    )

    running(theServer) {
      assert(get(root) === "bar") //method matched
      assert(post(remoteUrl("/foo"), "") === "bar") //uri matched
      assert(statusCode("POST", remoteUrl("/blah")) === 400) // none was matched
    }
  }

  test("should match put method via api") {
    theServer record (
      when(method -> "PUT") then "bar"
    )

    running(theServer) {
      assert(put(root) === "bar")
    }
  }

  test("should match delete method via api") {
    theServer record (
      when(method -> "DELETE") then "bar"
    )

    running(theServer) {
      assert(delete(root) === "bar")
    }
  }

  test("should return resource one by one") {
    theServer record (
      when(uri -> "/foo") then ("first", "second", "third")
    )

    running(theServer) {
      assert(get(remoteUrl("/foo")) === "first")
      assert(get(remoteUrl("/foo")) === "second")
      assert(get(remoteUrl("/foo")) === "third")
    }
  }
}
