package org.github.nicholasren.moco.scala

import org.scalatest.FunSuite

import com.github.dreamhead.moco.Moco._
import org.github.nicholasren.moco.scala.MocoScala._
import org.github.nicholasren.moco.scala.helper.RemoteTestHelper._

class MocoScalaTest extends FunSuite {
  val server = httpserver(port)

  test("should return expected response") {
    server response "foo"

    running(server, () => {
      val response = get(root)
      assert(response === "foo")
    })
  }

  test("should return expected response with text api ") {
    server response text("foo")

    running(server, () => {
      assert(get(root) === "foo");
    })
  }

}
