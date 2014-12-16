package com.github.nicholasren.moco.wrapper

import com.github.dreamhead.moco.{ResponseHandler, RequestMatcher}

case class Rule(matcher: Option[RequestMatcher], handler: ResponseHandler)

object Rule{
  def default(handler: ResponseHandler) = Rule(None, handler)
}
