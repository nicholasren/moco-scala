package com.github.nicholasren.moco.wrapper

import org.scalatest._


class SMocoConfigTest extends FlatSpec {

  "moco config" should "set file root" in {
    val origin = new SMocoConfig("fileRoot")("root").origin
    assert(origin.apply("file") === "root/file")
  }
}
