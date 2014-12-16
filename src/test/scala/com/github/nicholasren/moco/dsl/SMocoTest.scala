package com.github.nicholasren.moco.dsl

import org.scalatest._
import Matchers._
import org.scalatest.mock.MockitoSugar
import com.github.dreamhead.moco.config.{MocoContextConfig, MocoFileRootConfig}
import com.github.dreamhead.moco.{ResponseHandler, RequestMatcher, MocoConfig}
import com.github.nicholasren.moco.wrapper.ExtractorMatcher
import com.github.nicholasren.moco.dsl.Conversions._

class SMocoTest extends FlatSpec with MockitoSugar {
  val server = new SMoco()

  "a config api" should "capture multiple configs" in {
     val conf1 = mock[MocoConfig[_]]
     val conf2 = mock[MocoConfig[_]]
     val configs = new CompositeMocoConfig(Seq(conf1, conf2))

     server.configs(configs)

     server.confs should equal (Seq(conf1, conf2))
  }

  "a file root config api" should "generate a file root config" in {
    val config = SMoco.fileRoot("root")

    config shouldBe a [MocoFileRootConfig]
  }

  "a context config api" should "generate a context config" in {
    val config = SMoco.context("hello")

    config shouldBe a [MocoContextConfig]
  }

  "a form matcher" should "be a extractor matcher" in {
    SMoco.form("name") shouldBe a [ExtractorMatcher]
  }

  "a xml matcher" should "be a request matcher" in {
    SMoco.xml(SMoco.file("filename")) shouldBe a [RequestMatcher]
  }

  "a xpath matcher" should "be a extractor matcher" in {
    SMoco.xpath("/request/parameters/id/text()") shouldBe a [ExtractorMatcher]
  }

  "a json matcher" should "be a request matcher" in {
    SMoco.json(SMoco.file("filename")) shouldBe a [RequestMatcher]
  }

  "a jsonpath matcher" should "be a extractor matcher" in {
    SMoco.jsonPath("$.book[*].price") shouldBe a [ExtractorMatcher]
  }

  "a proxy" should "be a response handler" in {
    SMoco.proxy("http://github.com") shouldBe a [ResponseHandler]
  }

  "a proxy with failover" should "be a response handler" in {
    SMoco.proxy("http://github.com") {
      SMoco.failover("failover-filename")
    } shouldBe a [ResponseHandler]
  }

  "a proxy with playback" should "be a response handler" in {
    SMoco.proxy("http://github.com") {
      SMoco.playback("playback-filename")
    } shouldBe a [ResponseHandler]
  }

  "a proxy with from and to" should "be a response handler" in {
    SMoco.proxy {
      SMoco.from("local-base") to("remote-base")
    } shouldBe a [ResponseHandler]
  }

}
