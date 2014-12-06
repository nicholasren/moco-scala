package com.github.nicholasren.moco.dsl

import org.scalatest.{FunSpec, BeforeAndAfter}

import SMoco._
import Conversions._

import org.apache.http.client.fluent.Request
import org.github.nicholasren.moco.helper.RemoteTestHelper


class ResponseHandlerTest extends FunSpec with BeforeAndAfter with RemoteTestHelper {

  var theServer: SMoco = null

  val port = 8080

  before {
    theServer = server(port)
  }

  describe("responses") {
    it("send text") {
      theServer when {
        method("get")
      } then {
        text("get")
      }


      theServer running {
        assert(get(root) === "get")
      }
    }

    it("send headers") {
      theServer when {
        method("get")
      } then {
        headers("Content-Type" -> "json", "Accept" -> "html")
      }


      theServer running {
        assert(getForHeader("Content-Type") === "json")
        assert(getForHeader("Accept") === "html")
      }
    }

    it("send content in seq") {
      theServer when {
        method("get")
      } then {
        seq("foo", "bar", "baz")
      }


      theServer running {
        assert(get(root) === "foo")
        assert(get(root) === "bar")
        assert(get(root) === "baz")
      }
    }

    it("send multi response handler") {
      theServer when {
        method("get")
      } then {
        status(201) and text("hello")
      }


      theServer running {
        assert(getForStatus(root) === 201)
        assert(get(root) === "hello")
      }
    }

    it("send version") {
      theServer when {
        method("get")
      } then {
        version("HTTP/1.0")
      }

      theServer running {
        val version = Request.Get(root).execute.returnResponse.getProtocolVersion

        assert(version.getMajor === 1)
        assert(version.getMinor === 0)
      }
    }
  }
}