package com.github.nicholasren.moco.dsl

import org.scalatest._
import Matchers._
import org.scalatest.mock.MockitoSugar
import com.github.dreamhead.moco.config.{MocoContextConfig, MocoFileRootConfig}
import com.github.dreamhead.moco.MocoConfig
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

  "a form matcher" should "capture matcher" in {
    SMoco.form("name") shouldBe a [ExtractorMatcher]
  }
}
