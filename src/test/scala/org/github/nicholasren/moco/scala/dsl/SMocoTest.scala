package org.github.nicholasren.moco.scala.dsl

import org.scalatest.{BeforeAndAfter, FunSuite}

import SMoco._
import org.github.nicholasren.moco.scala.helper.RemoteTestHelper._
import SRequestMatcher._
import SResource._
import com.google.common.io.ByteStreams

class SMocoTest extends FunSuite with BeforeAndAfter {

  test("should return expected response") {
    val theServer = server(8080) {
      response("bar")
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
      response {
        file("src/test/resources/bar.response")
      }
    }

    running(theServer) {
      assert(get(root) === "bar")
    }
  }
}
