package org.github.nicholasren.moco.scala.dsl

import com.github.dreamhead.moco.RequestMatcher
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
  def running(smoco: SMoco)(block: => Unit) = {
    val server = new MocoHttpServer(smoco.server.asInstanceOf[ActualHttpServer])
    try {
      server.start
      block
    }
    finally server.stop
  }
}

