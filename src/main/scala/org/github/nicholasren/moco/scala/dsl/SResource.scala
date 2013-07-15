package org.github.nicholasren.moco.scala.dsl

import com.github.dreamhead.moco.{Moco, RequestMatcher}
import com.github.dreamhead.moco.resource.{TextResource, Resource}

object SResource {
  def file(name: String) = Moco.file(name)
  implicit def stringToResource(string: String): TextResource = Moco.text(string)
}
