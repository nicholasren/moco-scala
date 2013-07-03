package org.github.nicholasren.moco.scala.dsl

import com.github.dreamhead.moco.RequestMatcher

object server {
  def apply(port: Int)(configs: => (Option[RequestMatcher], String)) = {
    new SMoco(port, configs)
  }
}

object when {
  def apply(condition: => RequestMatcher) = new {
    def andThen(content: => String): (Option[RequestMatcher], String) = (Some(condition), content)
  }
}

object response {
  def apply(content: => String) = (None, content)
}
