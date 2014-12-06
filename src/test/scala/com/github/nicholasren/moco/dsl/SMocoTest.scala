package com.github.nicholasren.moco.dsl

import org.scalatest.FlatSpec
import com.github.nicholasren.moco.wrapper.SMocoConfig
import org.scalatest.mock.MockitoSugar

class SMocoTest extends FlatSpec with MockitoSugar {

  "a configured SMoco" should "start server with configs" in {
    val smoco = new SMoco()
    val configs = mock[SMocoConfig]

    smoco.config(configs)
  }
}
