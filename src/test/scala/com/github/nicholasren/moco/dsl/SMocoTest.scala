package com.github.nicholasren.moco.dsl

import org.scalatest._
import Matchers._
import org.scalatest.mock.MockitoSugar
import com.github.dreamhead.moco.config.{MocoContextConfig, MocoFileRootConfig}

class SMocoTest extends FlatSpec with MockitoSugar {

  "a file root config api" should "generate a file root config" in {
    val config = SMoco.fileRoot("root")

    config shouldBe a [MocoFileRootConfig]
  }

  "a context config api" should "generate a context config" in {
    val config = SMoco.context("hello")

    config shouldBe a [MocoContextConfig]
  }
}
