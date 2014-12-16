package com.github.nicholasren.moco.wrapper

import com.github.dreamhead.moco.{ResponseHandler, RequestMatcher}
import com.github.nicholasren.moco.dsl.SMoco

class PartialRule(matcher: RequestMatcher, moco: SMoco) {

  def then(handler: ResponseHandler): SMoco = {
    moco.record(new Rule(Some(matcher), handler))
    moco
  }
}
