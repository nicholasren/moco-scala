package org.github.nicholasren.moco.scala.dsl

import com.github.dreamhead.moco.{RequestMatcher, ResponseHandler, HttpServer}
import com.github.dreamhead.moco.internal.{ActualHttpServer, MocoHttpServer}
import com.github.dreamhead.moco.handler.ResponseHandlers._

class Moco(httpServer: HttpServer) {

  def request(block: => RequestMatcher) = {
    httpServer request block
    this
  }

  def response(block: => ResponseHandler) = {
    httpServer response block
    this
  }

  def running(block: => Unit) = {
    val server = new MocoHttpServer(httpServer.asInstanceOf[ActualHttpServer])
    try {
      server.start
      block
    }
    finally server.stop
  }

}

object Moco {
  def httperver(port: Int): Moco = {
    new Moco(com.github.dreamhead.moco.Moco.httpserver(port))
  }

  def file(path: String) = {
    responseHandler(com.github.dreamhead.moco.Moco.file(path))
  }

  def text(text: String) = {
    responseHandler(com.github.dreamhead.moco.Moco.text(text))
  }

  def by(content: String) = {
    com.github.dreamhead.moco.Moco.by(content)
  }
}
