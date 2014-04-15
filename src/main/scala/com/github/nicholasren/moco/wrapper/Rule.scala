package com.github.nicholasren.moco.wrapper

import com.github.dreamhead.moco.{ResponseHandler, RequestMatcher}

case class Rule(matcher: RequestMatcher, handler: ResponseHandler)
