package org.github.nicholasren.moco.scala.dsl

import com.github.dreamhead.moco.RequestMatcher
import com.github.dreamhead.moco.internal.{ActualHttpServer, MocoHttpServer}

class SMoco(port: Int, configs: => (Option[RequestMatcher], String)) {
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

