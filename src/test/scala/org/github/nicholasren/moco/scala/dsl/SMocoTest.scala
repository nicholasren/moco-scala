package org.github.nicholasren.moco.scala.dsl

import org.scalatest.{FunSpec, BeforeAndAfter}

import SMoco._
import com.google.common.io.ByteStreams
import java.lang.String
import org.apache.http.client.fluent.Request
import org.apache.http.HttpVersion
import org.github.nicholasren.moco.scala.helper.RemoteTestHelper._

class SMocoTest extends FunSpec with BeforeAndAfter {
  var theServer: SMoco = server(8080)

  before {
    theServer = server(8080)
  }

  describe("moco server"){
    describe("should return expected response"){
      it("based on specified uri") {
        theServer record (
          get("/foo") then "bar",
          get("/hello") then "world"
        )

        running(theServer) {
          assert(sendGet(remoteUrl("/foo")) === "bar")
          assert(sendGet(remoteUrl("/hello")) === "world")
        }
      }

      describe("based on specified method") {
        it("response to GET"){
          theServer record (
            when(method -> "GET") then "foo"
          )

          running(theServer) {
            assert(sendGet(root) === "foo")
            assert(statusCode("POST", root) === 400)
          }
        }

        it("response to PUT") {
          theServer record (
            when(method -> "PUT") then "bar"
          )

          running(theServer) {
            assert(put(root) === "bar")
          }
        }

        it("response to DELETE") {
          theServer record (
            when(method -> "DELETE") then "bar"
          )

          running(theServer) {
            assert(delete(root) === "bar")
          }
        }
      }

      it("based on path resource") {
        theServer record (
          when(pathResource -> "foo.request") then "bar"
          )

        running(theServer) {
          val asStream = this.getClass().getClassLoader().getResourceAsStream("foo.request");

          assert(postBytes(root, ByteStreams.toByteArray(asStream)) === "bar")
        }
      }

      it("based on content"){
        theServer record (
          when(content -> "foo") then "bar"
          )

        running(theServer) {
          assert(sendPost(root, "foo") === "bar")
        }
      }

      it("based on file"){
        theServer record (
          when(content -> file("src/test/resources/foo.request")) then "bar"
        )

        running(theServer) {
          assert(sendPost(root, "a foo request") === "bar")
        }
      }

      it("based on parameters") {
        theServer record (
          when(param("foo") -> "bar") then "bar",
          when(param("param") -> "value") then "value"
        )

        running(theServer) {
          assert(sendGet(remoteUrl("/index?foo=bar")) === "bar")
          assert(sendGet(remoteUrl("/index?param=value")) === "value")
        }
      }

      it("based on headers") {
        theServer record (
          when(header("foo") -> "bar") then "bar"
        )

        running(theServer) {
          assert(requestWithHeader("foo", "bar") === "bar")
        }
      }

      it("based on http version") {
        theServer record (
          when(version -> "HTTP/1.1") then "1.1",
          when(version -> "HTTP/1.0") then "1.0"
        )

        running(theServer) {
          assert(sendGet(root) === "1.1")
          val content: String = Request.Get(root).version(HttpVersion.HTTP_1_0).execute.returnContent.asString
          assert(content == "1.0")
        }
      }

      describe("based on multiple matchers"){
        it("with AND") {
          theServer record (
            when(uri -> "/foo" and method -> "GET")
              then "bar"
            )

          running(theServer) {
            assert(sendGet(remoteUrl("/foo")) === "bar")
            assert(statusCode("POST", root) === 400)
          }
        }

        it("with OR") {
          theServer record (
            when(uri -> "/foo" or method -> "GET") then "bar"
            )

          running(theServer) {
            assert(sendGet(root) === "bar") //method matched
            assert(sendPost(remoteUrl("/foo"), "") === "bar") //uri matched
            assert(statusCode("POST", remoteUrl("/blah")) === 400) // none was matched
          }
        }
      }

      ignore("based on cookie"){
        theServer record (
          when(cookie("logged_in") -> "true") then "bar"
        )

        running(theServer) {
          assert(sendGet(root) === "bar")
        }
      }

      it("based on form"){
        theServer record (
          post(form("name") -> "foo") then "bar"
        )

        running(theServer) {
          assert(postForm("name", "foo") === "bar")
        }
      }

    }

    describe("should return default response"){
      it("when only default was configured") {
        theServer record (default("bar"))
        running(theServer) {
          assert(sendGet(root) === "bar")
        }
      }

      it("when no rules are matched") {
        theServer record (
          when(uri -> "/foo") then "bar",
          default("hello")
        )

        running(theServer) {
          assert(sendGet(root) === "hello")
        }
      }
    }

    describe("should return response"){
      it("from file") {
        theServer record (
          default(file("src/test/resources/bar.response"))
          )

        running(theServer) {
          assert(sendGet(root) === "bar")
        }
      }

      it("one by one") {
        theServer record (
          default(seq("first", "second", "third"))
        )

        running(theServer) {
          assert(sendGet(root) === "first")
          assert(sendGet(root) === "second")
          assert(sendGet(root) === "third")
        }
      }
    }
  }


}
