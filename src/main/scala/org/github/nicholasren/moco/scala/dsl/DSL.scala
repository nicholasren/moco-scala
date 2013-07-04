package org.github.nicholasren.moco.scala.dsl

import com.github.dreamhead.moco.{Moco, RequestMatcher}
import com.github.dreamhead.moco.resource.Resource

object server {
  def apply(port: Int)(configs: => (Option[RequestMatcher], Resource)) = {
    new SMoco(port, configs)
  }
}

object when {
  def apply(condition: => RequestMatcher) = new {
    def then(content: => Resource): (Option[RequestMatcher], Resource) = (Some(condition), content)
  }
}

object response {
  def apply(content: => Resource) = (None, content)
}


object SResource {
  def file(name: String) = Moco.file(name)
  def text(content: String) = Moco.text(content)
}
