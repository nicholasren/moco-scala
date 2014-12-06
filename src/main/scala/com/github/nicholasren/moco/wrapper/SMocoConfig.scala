package com.github.nicholasren.moco.wrapper

import com.github.dreamhead.moco.MocoConfig
import com.github.dreamhead.moco.config.MocoFileRootConfig

class SMocoConfig(name:String)(value: String) {
  def origin: MocoConfig[String] = new MocoFileRootConfig(value)
}
