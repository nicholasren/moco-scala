package com.github.nicholasren.moco.features

import org.scalatest.{BeforeAndAfter, GivenWhenThen, FeatureSpec}

import org.github.nicholasren.moco.helper.RemoteTestHelper

class ConfigTest extends FeatureSpec
with GivenWhenThen with BeforeAndAfter
with RemoteTestHelper {

  info("As a api consumer")
  info("I want to be able to config my mock server")
  info("So I these configs can be shared by all examples")

  val port = 8082

  feature("config file root") {

    scenario("serving file under configured root") {
//      val theServer = server(port)
//      theServer config {
//        fileRoot "src/test/resources"
//      }
//
//      theServer when {
//        method("get")
//      } then {
//        file("bar.response")
//      }
//
//
//      theServer running {
//        assert(get(remoteUrl("/")) === "bar")
//      }
    }
  }
}
