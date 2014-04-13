package com.github.nicholasren.moco.scala.dsl

import org.scalatest.{ FunSpec, BeforeAndAfter }

import SMoco._
import org.github.nicholasren.moco.scala.helper.RemoteTestHelper._

class SMocoTest extends FunSpec with BeforeAndAfter {
  val theServer: ServerSetting = server(8080)

  describe("moco server") {
    describe("matchers") {

      it("match by uri") {
        theServer record {
          when {
            uri("/hello")
            method("post")
          } then {
            status(200)
            text("world")
          }
        }

        theServer running {
          assert(statusCode("get", remoteUrl("/hello")) === 400)
          assert(sendPost(remoteUrl("/hello"), "") === "world")
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
          assert(sendGet(remoteUrl("/hello123")) === "world")
          assert(sendGet(remoteUrl("/helloabc")) === "world")
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
          assert(sendGet(root) === "get")
        }
      }

      it("match by headers") {
        theServer record {
          when {
            headers("Content-Type" -> "json", "Accept" -> "json")
          } then {
            text("headers matched")
          }
        }

        theServer running {
          assert(requestWithHeaders("Content-Type" -> "json", "Accept" -> "json") === "headers matched")
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
          assert(sendGet(root) === "get")
          assert(sendPost(root, "") === "post")
        }
      }
    }

    describe("responses") {

    }
  }

}
