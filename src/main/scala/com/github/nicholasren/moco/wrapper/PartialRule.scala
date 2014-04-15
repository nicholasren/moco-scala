package com.github.nicholasren.moco.wrapper

import com.github.dreamhead.moco.{ResponseHandler, RequestMatcher}
import com.github.dreamhead.moco.handler.ResponseHandlers
import com.github.dreamhead.moco.resource.Resource
import com.github.nicholasren.moco.dsl.SMoco

class PartialRule(matcher: RequestMatcher, moco: SMoco) {

  def then(handler: ResponseHandler): SMoco = {
    moco.record(new Rule(matcher, handler))
    moco
  }
}
