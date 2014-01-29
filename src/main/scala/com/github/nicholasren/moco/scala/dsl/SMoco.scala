package com.github.nicholasren.moco.scala.dsl

import com.github.dreamhead.moco.{ResponseHandler, Moco, RequestMatcher, HttpServer}
import com.github.dreamhead.moco.internal.{ActualHttpServer, MocoHttpServer}
import com.github.dreamhead.moco.resource.{TextResource, Resource}
import com.github.dreamhead.moco.handler.ResponseHandlers._
import scala.Option

class SMoco(port: Int) {
  type Rule = (Option[RequestMatcher], ResponseHandler)

   val server = com.github.dreamhead.moco.Moco.httpserver(port)

//  def record(rules: Rule*) {
//    rules.foreach(rule => {
//      val (matcher, responseHandler) = rule
//      matcher match {
//        case Some(_) => server.request(matcher.get).response(responseHandler)
//        case _ => server.response(responseHandler)
//      }
//    })
//  }

  def apply(rules:  => Unit) = {
    rules
  }
}

case class When(matcher: Option[RequestMatcher]) {
  def then(content: Resource) = (matcher, responseHandler(content))

  def then(handler: ResponseHandler) = (matcher, handler)
}

object SMoco {

  implicit def stringToResource(string: String): Resource = Moco.text(string)
  implicit val aServer = com.github.dreamhead.moco.Moco.httpserver(1000)

//  implicit def enrich(origin: RequestMatcher) = new RichRequestMatcher(origin)

  def server(port: Int) = {
    new SMoco(port)
  }

  def running(testFun: => Unit)(implicit moco: SMoco) = {
    val server = new MocoHttpServer(moco.server.asInstanceOf[ActualHttpServer])
    try {
      server.start
      testFun
    }
    finally server.stop
  }

//  def post(matcher: RequestMatcher) = new When(Some(enrich(matcher) and method -> "post"))

  def get(theUri: String)(content: => String)(implicit smoco: SMoco) = {
    smoco.server.request(Moco.by(Moco.uri(theUri))).response(content)
  }

  def put(theUri: String)(callback: => String)(implicit smoco: SMoco) = {
    import Moco._
    smoco.server.request(and(by(uri(theUri)), by(method("put")))).response(callback)
  }
}

