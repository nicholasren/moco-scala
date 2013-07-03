package org.github.nicholasren.moco.scala.dsl

import org.scalatest.{BeforeAndAfter, FunSuite}

import SMoco._
import org.github.nicholasren.moco.scala.helper.RemoteTestHelper._
import SRequestMatcher._

class MocoTest extends FunSuite with BeforeAndAfter {


  test("should return expected response") {
    val theServer = server(8080) {
      when(uri -> "/bar").then("foo")
    }

    running(theServer) {
      val response = get(remoteUrl("/bar"))
      assert(response === "foo")
    }
  }
}
