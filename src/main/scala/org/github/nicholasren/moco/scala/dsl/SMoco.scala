package org.github.nicholasren.moco.scala.dsl

import com.github.dreamhead.moco.{Moco, RequestMatcher}
import com.github.dreamhead.moco.internal.{ActualHttpServer, MocoHttpServer}
import com.github.dreamhead.moco.resource.Resource

class SMoco(port: Int, configs: => (Option[RequestMatcher], Resource)) {
  val server = com.github.dreamhead.moco.Moco.httpserver(port)
  val (matcher, response) = configs

  matcher match {
    case Some(_) => server.request(matcher.get).response(response)
    case _ => server.response(response)
  }
}

object SMoco {

  def server(port: Int)(configs: => (Option[RequestMatcher], Resource)) = {
    new SMoco(port, configs)
  }

  def when(condition: RequestMatcher) = new {
    def then(content: Resource): (Option[RequestMatcher], Resource) = (Some(condition), content)
  }

  def response(content: Resource) = (None, content)

  def file(name: String) = Moco.file(name)

  def running(moco: SMoco)(block: => Unit) = {
    val server = new MocoHttpServer(moco.server.asInstanceOf[ActualHttpServer])
    try {
      server.start
      block
    }
    finally server.stop
  }

  implicit def stringToResource(string: String) = Moco.text(string)
}

