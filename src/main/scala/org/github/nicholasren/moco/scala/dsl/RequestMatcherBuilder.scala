package org.github.nicholasren.moco.scala.dsl

import com.github.dreamhead.moco.{Moco, RequestMatcher}

trait RequestMatcherBuilder {
  def ->(value: String): RequestMatcher
}

class RichRequestMatcher(origin: RequestMatcher) {
  def and(other: RequestMatcher) = Moco.and(this.origin, other)

  def or(other: RequestMatcher) = Moco.or(this.origin, other)
}

object RequestMatcherBuilder {

  implicit def enrich(origin: RequestMatcher) = new RichRequestMatcher(origin)

  def uri = new RequestMatcherBuilder {

    def ->(value: String): RequestMatcher = {
      val resource = Moco.uri(value)
      Moco.by(resource)
    }
  }

  def method = new RequestMatcherBuilder {
    def ->(value: String): RequestMatcher = {
      val resource = Moco.method(value)
      Moco.by(resource)
    }
  }

  def pathResource = new RequestMatcherBuilder {
    def ->(value: String): RequestMatcher = {
      val pathResource = Moco.pathResource(value)
      Moco.by(pathResource)
    }
  }
}
