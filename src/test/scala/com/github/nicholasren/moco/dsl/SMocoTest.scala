package com.github.nicholasren.moco.dsl

import org.scalatest.FlatSpec
import com.github.nicholasren.moco.wrapper.SMocoConfig
import org.scalatest.mock.MockitoSugar

class SMocoTest extends FlatSpec with MockitoSugar {

  "a configured SMoco" should "memorize these configs" in {
    val smoco = new SMoco()

    val configs = mock[SMocoConfig]

    smoco.config(configs)

    assert(smoco.config === configs)
  }

  "a file root config api" should "generate a file root config" in {
    val path = "root"
    val config = SMoco.fileRoot(path)

    assert(config.value === path)
  }
}
