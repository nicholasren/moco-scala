package features

import org.scalatest.{FunSpec, BeforeAndAfter}


import org.apache.http.client.fluent.Request
import org.github.nicholasren.moco.helper.RemoteTestHelper
import com.github.nicholasren.moco.dsl.SMoco
import com.github.nicholasren.moco.dsl.SMoco._
import com.github.nicholasren.moco.dsl.Conversions._

import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

class ResponseHandlerTest extends FunSpec with BeforeAndAfter with RemoteTestHelper {

  var theServer: SMoco = null

  val port = 8080

  before {
    theServer = server(port)
  }

  describe("default") {
    it("send default response") {
      theServer default {
        text("default")
      }

      theServer running {
        assert(get(root) === "default")
      }
    }
  }

  describe("redirect") {
    it("redirect to expected url") {

      theServer when {
        uri("/")
      } then {
        text("foo")
      } when {
        uri("/redirect")
      } then redirectTo("/")

      theServer running {
        assert(get(remoteUrl("/redirect")) === "foo")
      }
    }
  }

  describe("latency") {

    it("wait for a while") {
      val duration = Duration(1, TimeUnit.SECONDS)

      theServer default {
        latency(duration)
      }

     theServer running {

       val start = System.currentTimeMillis()
       getForStatus(root)
       val stop = System.currentTimeMillis()

       assert((stop - start) >= duration.toMillis)
     }
    }
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