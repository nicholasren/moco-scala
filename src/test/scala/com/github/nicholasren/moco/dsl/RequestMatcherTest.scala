package com.github.nicholasren.moco.dsl

import org.scalatest.{BeforeAndAfter, FunSpec}
import org.github.nicholasren.moco.helper.RemoteTestHelper._
import org.apache.http.HttpVersion
import SMoco._
import Conversions._

class RequestMatcherTest extends FunSpec with BeforeAndAfter {

  var theServer: SMoco = null

  before {
    theServer = server(8080)
  }

  describe("matchers") {

    it("match by uri") {
      theServer record {
        when {
          uri("/hello") and method("post")
        } then {
          status(200) and text("world")
        }
      }

      theServer running {
        assert(getForStatus(remoteUrl("/hello")) === 400)
        assert(post(remoteUrl("/hello"), "") === "world")
      }
    }

    it("match uri by regex") {
      theServer record {
        when {
          uri matched "/hello.+"
        } then {
          text("world")
        }
      }

      theServer running {
        assert(get(remoteUrl("/hello123")) === "world")
        assert(get(remoteUrl("/hello-abc")) === "world")
      }
    }

    it("match header by regex") {
      theServer record {
        when {
          header("Content-Type") matched ".+json"
        } then {
          text("headers matched")
        }
      }

      theServer running {
        assert(getWithHeaders("Content-Type" -> "application/json") === "headers matched")
        assert(getForStatusWithHeaders("Content-Type" -> "html/text") === 400)
      }
    }

    it("match text by regex") {
      theServer record {
        when {
          text matched "hello.+"
        } then {
          text("text matched")
        }
      }

      theServer running {
        assert(post(root, "hello-abc") === "text matched")
        assert(post(root, "hello-123") === "text matched")
      }

    }

    it("match by method") {
      theServer record {
        when {
          method("get")
        } then {
          text("get")
        }
      }

      theServer running {
        assert(get(root) === "get")
      }
    }

    it("match by headers") {
      theServer record {
        when {
          header("Content-Type") === "content-type"
        } then {
          text("headers matched")
        }
      }

      theServer running {
        assert(getWithHeaders("Content-Type" -> "content-type") === "headers matched")
      }
    }

    it("match by version") {
      theServer record {
        when {
          version("HTTP/1.0")
        } then {
          text("version matched")
        }
      }

      theServer running {
        assert(getWithVersion(root, HttpVersion.HTTP_1_0) === "version matched")
      }
    }

    it("can define multi matchers") {
      theServer record {
        List(
          when {
            method("get")
          } then {
            text("get")
          },
          when {
            method("post")
          } then {
            text("post")
          }
        )
      }

      theServer running {
        assert(get(root) === "get")
        assert(post(root, "") === "post")
      }
    }
  }
}