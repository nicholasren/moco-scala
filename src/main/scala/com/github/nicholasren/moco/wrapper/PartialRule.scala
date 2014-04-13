package com.github.nicholasren.moco.wrapper

import com.github.dreamhead.moco.{ResponseHandler, RequestMatcher}
import com.github.dreamhead.moco.handler.ResponseHandlers
import com.github.dreamhead.moco.resource.Resource

class PartialRule(matchers: List[RequestMatcher]) {

  def then(responses: Any*): Rule = {
    val handlers = responses.map {
      _ match {
        case resource: Resource => ResponseHandlers.responseHandler(resource)
        case handler: ResponseHandler => handler
      }
    }

    new Rule(this.matchers, handlers.toList)
  }
}
