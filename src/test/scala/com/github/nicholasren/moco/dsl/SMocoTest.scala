package com.github.nicholasren.moco.dsl

import org.scalatest.{ FunSpec, BeforeAndAfter }

import SMoco._
import Conversions._

import org.github.nicholasren.moco.helper.RemoteTestHelper._
import com.github.nicholasren.moco.wrapper.ServerSetting


class SMocoTest extends FunSpec with BeforeAndAfter {

  var theServer: ServerSetting = null

  before {
    theServer = server(8080)
  }

  describe("moco server") {
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
            matched {
              uri("/hello.+")
            }
          } then {
            status(200)
            text("world")
          }
        }

        theServer running {
          assert(get(remoteUrl("/hello123")) === "world")
          assert(get(remoteUrl("/helloabc")) === "world")
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
          assert(requestWithHeaders("Content-Type" -> "content-type") === "headers matched")
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

    describe("responses") {
      it("send text") {
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

      it("send headers") {
        theServer record {
          when {
            method("get")
          } then {
            headers("Content-Type" -> "json", "Accept" -> "html")
          }
        }

        theServer running {
          assert(getForHeader("Content-Type") === "json")
          assert(getForHeader("Accept") === "html")
        }
      }

      it("send content in seq") {
        theServer record {
          when {
            method("get")
          } then {
            seq("foo", "bar", "baz")
          }
        }

        theServer running {
          assert(get(root) === "foo")
          assert(get(root) === "bar")
          assert(get(root) === "baz")
        }
      }

      it("send multi response handler") {
        theServer record {
          when {
            method("get")
          } then {
            status(201) and text("hello")
          }
        }

        theServer running {
          assert(getForStatus(root) === 201)
          assert(get(root) === "hello")
        }
      }
    }
  }
}
