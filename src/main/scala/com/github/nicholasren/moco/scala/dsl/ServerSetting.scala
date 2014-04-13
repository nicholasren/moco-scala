package com.github.nicholasren.moco.scala.dsl

import com.github.dreamhead.moco.{ResponseHandler, Moco, RequestMatcher}
import com.github.dreamhead.moco.internal.{ActualHttpServer, MocoHttpServer}
import com.github.dreamhead.moco.resource.Resource
import com.github.dreamhead.moco.matcher.AndRequestMatcher
import com.github.dreamhead.moco.handler.AndResponseHandler
import com.github.dreamhead.moco.handler.ResponseHandlers

import scala.collection.JavaConversions._

case class Rule(matchers: List[RequestMatcher], handlers: List[ResponseHandler])

class PartialRule(matchers: List[RequestMatcher]) {

  def then(responses: Any*): Rule = {
    val handlers = responses.map {
      _ match {
        case resource: Resource => ResponseHandlers.responseHandler(resource)
        case handler: ResponseHandler => handler
      }
    }

    new Rule(this.matchers, handlers.toList)
  }
}

class ServerSetting(port: Int) {

  val server = com.github.dreamhead.moco.Moco.httpserver(port)

  def running(testFun: => Unit) = {
    val theServer = new MocoHttpServer(server.asInstanceOf[ActualHttpServer])
    theServer.start

    try {
      testFun
    }
    finally {
      theServer.stop
    }
  }

  def record(rules: List[Rule]) {
    rules.foreach(rule => {
      val wrappedMatchers = new AndRequestMatcher(rule.matchers)
      val wrappedHandlers = new AndResponseHandler(rule.handlers)

      server.request(wrappedMatchers).response(wrappedHandlers)
    })
  }

  def record(rule: Rule) {
    record(List(rule))
  }
}


object SMoco {

  def server(port: Int) = new ServerSetting(port)

  def when(conditions: Any*) = {
    val matchers = conditions.map {
      condition => condition match {
        case resource: Resource => Moco.by(resource)
        case matcher: RequestMatcher => matcher
      }
    }

    new PartialRule(matchers.toList)
  }

  //resources
  def uri(value: String): Resource = Moco.uri(value)

  def method(value: String): Resource = Moco.method(value)

  def matched(value: Resource) = Moco.`match`(value)

  def text(value: String): Resource = Moco.text(value)

  //request matcher
  def headers(headers: Tuple2[String, String]*): RequestMatcher = {
    val matchers = headers.map {
      header: Tuple2[String, String] =>
        Moco.eq(Moco.header(header._1), header._2)
    }
    new AndRequestMatcher(matchers)
  }


  //handlers

  def status(code: Int): ResponseHandler = Moco.status(code)
}

