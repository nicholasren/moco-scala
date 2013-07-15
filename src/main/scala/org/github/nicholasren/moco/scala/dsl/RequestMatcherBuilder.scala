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

object RequestMatcherBuilder {
  implicit def enrich(origin: RequestMatcher) = new RichRequestMatcher(origin)

  def uri = new RequestMatcherBuilder {

    def ->(value: String): RequestMatcher = Moco.by(Moco.uri(value))
  }

  def method = new RequestMatcherBuilder {
    def ->(value: String): RequestMatcher = Moco.by(Moco.method(value))
  }

  def pathResource = new RequestMatcherBuilder {
    def ->(value: String): RequestMatcher = Moco.by(Moco.pathResource(value))
  }

  def content = new RequestMatcherBuilder {
    def ->(value: String): RequestMatcher = Moco.by(Moco.text(value))
  }

  def param(name: String) = new RequestMatcherBuilder {
    def ->(value: String): RequestMatcher = Moco.eq(Moco.query(name), value)
  }

  def version = new RequestMatcherBuilder {
    def ->(value: String): RequestMatcher = Moco.by(Moco.version(value))
  }

  def header(name: String) = new RequestMatcherBuilder {
    def ->(value: String): RequestMatcher = Moco.eq(Moco.header(name), value)
  }

  def cookie(name: String) = new RequestMatcherBuilder {
    def ->(value: String): RequestMatcher = ???
  }

  def form(name: String) = new RequestMatcherBuilder {
    def ->(value: String): RequestMatcher = Moco.eq(Moco.form(name), value)
  }
}
