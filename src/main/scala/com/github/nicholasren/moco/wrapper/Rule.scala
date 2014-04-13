package com.github.nicholasren.moco.wrapper

import com.github.dreamhead.moco.{ResponseHandler, RequestMatcher}

case class Rule(matchers: List[RequestMatcher], handlers: List[ResponseHandler])
