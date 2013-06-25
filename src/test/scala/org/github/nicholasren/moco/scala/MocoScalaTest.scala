package org.github.nicholasren.moco.scala

import org.scalatest.FunSuite

import com.github.dreamhead.moco.Moco._
import org.apache.http.client.fluent.Request
import org.github.nicholasren.moco.scala.MocoScala._

class MocoScalaTest extends FunSuite {

  val server = httpserver(8080)

  test("should return expected response") {
    server.response("foo")

    running(server, () => {
      val uri: String = "http://localhost:8080"
      val rsp = Request.Get(uri).execute().returnContent().asString

      assert(rsp === "foo")
    });
  }


}
