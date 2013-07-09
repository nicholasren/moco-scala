package org.github.nicholasren.moco.scala.dsl

import com.github.dreamhead.moco.{ResponseHandler, Moco, RequestMatcher}
import com.github.dreamhead.moco.internal.{ActualHttpServer, MocoHttpServer}
import com.github.dreamhead.moco.resource.Resource
import com.github.dreamhead.moco.handler.ResponseHandlers._
import scala.Option

class SMoco(port: Int, configs: => (Option[RequestMatcher], ResponseHandler)) {
  val server = com.github.dreamhead.moco.Moco.httpserver(port)
  val (matcher, responseHandler) = configs

  matcher match {
    case Some(_) => server.request(matcher.get).response(responseHandler)
    case _ => server.response(responseHandler)
  }
}

case class When(matcher: RequestMatcher) {
  def then(content: Resource) = (Some(matcher), responseHandler(content))

  def then(handler: ResponseHandler) = (Some(matcher), handler)
}

object SMoco {

  def server(port: Int)(configs: => (Option[RequestMatcher], ResponseHandler)) = {
    new SMoco(port, configs)
  }

  def running(moco: SMoco)(block: => Unit) = {
    val server = new MocoHttpServer(moco.server.asInstanceOf[ActualHttpServer])
    try {
      server.start
      block
    }
    finally server.stop
  }

  def when(matcher: RequestMatcher) = new When(matcher)

  def default(content: Resource) = (None, responseHandler(content))

  def default(handler: ResponseHandler) = (None, handler)

  def file(name: String) = Moco.file(name)

  def seq(texts: String*) = Moco.seq(texts.map(Moco.text(_)): _*)

  implicit def stringToResource(string: String) = Moco.text(string)
}

