package org.github.nicholasren.moco.scala.dsl

import com.github.dreamhead.moco.{Moco, RequestMatcher}
import com.github.dreamhead.moco.resource.Resource

trait RequestMatcherBuilder {
  def ->(value: String): RequestMatcher

  def ->(resource: Resource): RequestMatcher = Moco.by(resource)
}

class RichRequestMatcher(origin: RequestMatcher) {
  def and(other: RequestMatcher) = Moco.and(this.origin, other)

  def or(other: RequestMatcher) = Moco.or(this.origin, other)
}