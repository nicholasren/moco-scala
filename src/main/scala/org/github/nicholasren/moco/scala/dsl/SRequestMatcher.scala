package org.github.nicholasren.moco.scala.dsl

import com.github.dreamhead.moco.{Moco, RequestMatcher}

trait SRequestMatcher {
  def ->(value: String): RequestMatcher
}

object SRequestMatcher {

  def uri = new SRequestMatcher {
    def ->(value: String): RequestMatcher = {
      val resource = Moco.uri(value)
      Moco.by(resource)
    }
  }

  def method = new SRequestMatcher {
    def ->(value: String): RequestMatcher = {
      val resource = Moco.method(value)
      Moco.by(resource)
    }
  }

  def pathResource = new SRequestMatcher {
    def ->(value: String): RequestMatcher = {
      val pathResource = Moco.pathResource(value)
      Moco.by(pathResource)
    }
  }
}
