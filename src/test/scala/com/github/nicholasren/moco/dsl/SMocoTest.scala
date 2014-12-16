package com.github.nicholasren.moco.dsl

import org.scalatest._
import Matchers._
import org.scalatest.mock.MockitoSugar
import com.github.dreamhead.moco.config.{MocoContextConfig, MocoFileRootConfig}
import com.github.dreamhead.moco.{RequestMatcher, MocoConfig}
import com.github.nicholasren.moco.dsl.Conversions.CompositeMocoConfig
import com.github.dreamhead.moco.extractor.FormRequestExtractor
import com.github.nicholasren.moco.wrapper.ExtractorMatcher

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

}
