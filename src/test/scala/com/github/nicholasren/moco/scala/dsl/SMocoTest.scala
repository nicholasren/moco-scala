package com.github.nicholasren.moco.scala.dsl

import org.scalatest.{ FunSpec, BeforeAndAfter }

import SMoco._
import com.google.common.io.ByteStreams
import java.lang.String
import org.apache.http.client.fluent.Request
import org.apache.http.HttpVersion
import org.github.nicholasren.moco.scala.helper.RemoteTestHelper._

class SMocoTest extends FunSpec with BeforeAndAfter {
  val theServer: ServerSetting = server(8080)

  describe("moco server") {
    describe("should return expected response") {

      it("match by uri and method") {
        theServer when (
          uri("/hello"),
          method("post")
        ) then (
          status(200),
          text("world")
        ) when (
          uri("/user")
        ) then (
          text("user")
        )

        theServer running {
          assert(statusCode("get", remoteUrl("/hello")) === 400)
          assert(sendPost(remoteUrl("/hello"), "") === "world")
          assert(sendGet(remoteUrl("/user")) === "user")
        }
      }

    }
  }

}
