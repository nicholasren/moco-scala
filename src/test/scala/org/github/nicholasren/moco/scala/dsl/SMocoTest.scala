package org.github.nicholasren.moco.scala.dsl

import org.scalatest.{BeforeAndAfter, FunSuite}

import SMoco._
import org.github.nicholasren.moco.scala.helper.RemoteTestHelper._
import SRequestMatcher._

class SMocoTest extends FunSuite with BeforeAndAfter {

  test("should return expected response") {
    val theServer = server(8080) {
      response("foo")
    }

    running(theServer) {
      assert(get(root) === "foo")
    }
  }



  test("should return expected response based on specified request uri") {
    val theServer = server(8080) {
      when(uri -> "/bar") andThen("foo")
    }

    running(theServer) {
      assert(get(remoteUrl("/bar")) === "foo")
    }
  }

  test("should return expected response based on specified request method") {
    val theServer = server(8080) {
      when(method -> "GET") andThen("foo")
    }

    running(theServer) {
      assert(get(remoteUrl("/")) === "foo")
    }

  }
}
