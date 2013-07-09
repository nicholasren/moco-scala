package org.github.nicholasren.moco.scala.dsl

import org.scalatest.{BeforeAndAfter, FunSuite}

import SMoco._
import org.github.nicholasren.moco.scala.helper.RemoteTestHelper._
import RequestMatcherBuilder._
import com.google.common.io.ByteStreams

class SMocoTest extends FunSuite with BeforeAndAfter {

  test("should return expected response") {
    val theServer = server(8080) {
      default("bar")
    }

    running(theServer) {
      assert(get(root) === "bar")
    }
  }

  test("should return expected response based on specified request uri") {
    val theServer = server(8080) {
      when(uri -> "/foo") then "bar"
    }

    running(theServer) {
      assert(get(remoteUrl("/foo")) === "bar")
    }
  }

  test("should return expected response based on specified request method") {
    val theServer = server(8080) {
      when(method -> "GET") then "foo"
    }

    running(theServer) {
      assert(get(root) === "foo")
      assert(statusCode("POST", root) === 400)
    }
  }

  test("should return expected response based on path resource") {
    val theServer = server(8080) {
      when(pathResource -> "foo.request") then "bar"
    }

    running(theServer) {
      val asStream = this.getClass().getClassLoader().getResourceAsStream("foo.request");

      assert(postBytes(root, ByteStreams.toByteArray(asStream)) === "bar")
    }
  }

  test("should return expected response from file") {
    val theServer = server(8080) {
      default(
        file("src/test/resources/bar.response")
      )
    }

    running(theServer) {
      assert(get(root) === "bar")
    }
  }

  test("should match request based on multiple matchers with AND operator") {
    val theServer = server(8080) {
      when(
        uri -> "/foo" and method -> "GET"
      ) then "bar"
    }

    running(theServer) {
      assert(get(remoteUrl("/foo")) === "bar")
      assert(statusCode("POST", root) === 400)
    }
  }

  test("should match request based on multiple matchers with OR operator") {
    val theServer = server(8080) {
      when(
        uri -> "/foo" or method -> "GET"
      ) then "bar"
    }

    running(theServer) {
      assert(get(root) === "bar") //method matched
      assert(post(remoteUrl("/foo"), "") === "bar") //uri matched
      assert(statusCode("POST", remoteUrl("/blah")) === 400) // none was matched
    }
  }

  test("should match put method via api") {
    val theServer = server(8080) {
      when(method -> "PUT") then "bar"
    }

    running(theServer) {
      assert(put(root) === "bar")
    }
  }

  test("should match delete method via api") {
    val theServer = server(8080) {
      when(method -> "DELETE") then "bar"
    }

    running(theServer) {
      assert(delete(root) === "bar")
    }
  }

  test("should return content one by one") {
    val theServer = server(8080) {
      when(uri -> "/foo") then seq("first", "second", "third")
    }

    running(theServer) {
      assert(get(remoteUrl("/foo")) === "first")
      assert(get(remoteUrl("/foo")) === "second")
      assert(get(remoteUrl("/foo")) === "third")
    }

  }
}
