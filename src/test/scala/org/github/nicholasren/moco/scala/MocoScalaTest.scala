package org.github.nicholasren.moco.scala

import org.scalatest.FunSuite

import com.github.dreamhead.moco.Moco._
import org.github.nicholasren.moco.scala.MocoScala._
import org.github.nicholasren.moco.scala.helper.RemoteTestHelper._

class MocoScalaTest extends FunSuite {
  val server = httpserver(port)

  test("should return expected response") {
    server response "foo"

    running(server) {
      val response = get(root)
      assert(response === "foo")
    }
  }

  test("should return expected response with text api ") {
    server response text("foo")
    running(server) {
      assert(get(root) === "foo")
    }
  }


  test("should_return_expected_response_based_on_specified_request") {
    server request(by("foo")) response("bar")

    running(server) {
      assert(post(root, "foo") === "bar")
    }
  }

  test("should return response based on specified uri") {
    server request(by(uri("/foo"))) response("bar")

    running(server) {
      assert(get(remoteUrl("/foo")) === "bar")
    }
  }
}
