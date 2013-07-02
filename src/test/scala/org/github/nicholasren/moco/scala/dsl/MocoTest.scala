package org.github.nicholasren.moco.scala.dsl

import org.scalatest.{BeforeAndAfter, FunSuite}

import Moco._
import org.github.nicholasren.moco.scala.helper.RemoteTestHelper._

class MocoTest extends FunSuite with BeforeAndAfter {

  test("should return expected response") {
    httperver(port) response {
      text("foo")
    } running {
      val response = get(root)
      assert(response === "foo")
    }
  }

  test("should return expected response with file api ") {
    httperver(port) response {
      file("src/test/resources/foo.response")
    } running {
      assert(get(root) === "foo")
    }
  }

//  test("should_return_expected_response_based_on_specified_request") {
//    httperver(port) request {
//      by("foo")
//    } response {
//      text("bar")
//    } running {
//      assert(post(root, "foo") === "bar")
//    }
//  }
}
