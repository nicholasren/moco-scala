package org.github.nicholasren.moco.scala.dsl

import com.github.dreamhead.moco.{ResponseHandler, Moco, RequestMatcher}
import com.github.dreamhead.moco.internal.{ActualHttpServer, MocoHttpServer}
import com.github.dreamhead.moco.resource.{TextResource, FileResource, Resource}
import com.github.dreamhead.moco.handler.ResponseHandlers._
import scala.Option


class SMoco(port: Int) {
  type Rule = (Option[RequestMatcher], ResponseHandler)

  val server = com.github.dreamhead.moco.Moco.httpserver(port)

  def record(configs: Rule*) {
    configs.foreach(config => {
      val (matcher, responseHandler) = config
      matcher match {
        case Some(_) => server.request(matcher.get).response(responseHandler)
        case _ => server.response(responseHandler)
      }
    })
  }
}

case class When(matcher: Option[RequestMatcher]) {
  def then(content: Resource) = (matcher, responseHandler(content))

  def then(handler: ResponseHandler) = (matcher, handler)

  def then(texts: String*) = (matcher, Moco.seq(texts.map(Moco.text(_)): _*))
}

object SMoco {

  def server(port: Int) = {
    new SMoco(port)
  }

  def running(moco: SMoco)(block: => Unit) = {
    val server = new MocoHttpServer(moco.server.asInstanceOf[ActualHttpServer])
    try {
      server.start
      block
    }
    finally server.stop
  }

  def when(matcher: RequestMatcher) = new When(Some(matcher))

  def default(resource: Resource) = whenDefault.then(responseHandler(resource))

  def default(handler: ResponseHandler) = whenDefault.then(handler)

  def default(texts: String*) = whenDefault.then(texts: _*)


  def file(name: String) = Moco.file(name)

  implicit def stringToResource(string: String): TextResource = Moco.text(string)

  private
  def whenDefault = new When(None)
}

