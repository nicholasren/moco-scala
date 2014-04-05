package com.github.nicholasren.moco.scala.dsl

import com.github.dreamhead.moco.{ResponseHandler, Moco, RequestMatcher}
import com.github.dreamhead.moco.internal.{ActualHttpServer, MocoHttpServer}
import com.github.dreamhead.moco.resource.{TextResource, Resource}
import com.github.dreamhead.moco.handler.ResponseHandlers._
import scala.Option
import com.github.dreamhead.moco.handler.ContentHandler
import com.github.dreamhead.moco.matcher.AndRequestMatcher
import scala.collection.JavaConversions._
import com.github.dreamhead.moco.handler.AndResponseHandler
import com.github.dreamhead.moco.handler.StatusCodeResponseHandler
import com.github.dreamhead.moco.handler.ResponseHandlers
import com.github.dreamhead.moco.Moco._

class ServerSetting(port: Int) {
  type Rule = (List[RequestMatcher], List[ResponseHandler])  

  val server = com.github.dreamhead.moco.Moco.httpserver(port)
  var rules: List[Rule] = List[Rule]()

  def running(testFun: => Unit) = {
    replay
    val theServer = new MocoHttpServer(server.asInstanceOf[ActualHttpServer])
    theServer.start
    
    try {  
      testFun
    }
    finally theServer.stop
  }
  
  def when(matchers: RequestMatcher*) = new When(matchers.toList, this)
  
  private
  def replay = {
    rules.foreach(rule => {
      val (matchers, handlers) = rule
      val wrappedMatchers = new AndRequestMatcher(asJavaIterable(matchers))
      val wrappedHandlers = new AndResponseHandler(asJavaIterable(handlers))
      server.request(wrappedMatchers).response(wrappedHandlers)
    })
  }
  
  def record(rule: Rule) = {
    this.rules = rule :: rules
    this
  }

}

class When(matchers: List[RequestMatcher], serverSetting: ServerSetting) {
  def then(handlers: ResponseHandler*) =  {
    serverSetting.record(matchers, handlers.toList)
  }
}

object SMoco {

  def server(port: Int) = new ServerSetting(port)

  //matchers
  def uri(value: String): RequestMatcher = by(Moco.uri(value))
  
  def method(value: String): RequestMatcher = by(Moco.method(value));
  
  //handlers
  def text(value: String): ResponseHandler = ResponseHandlers.responseHandler(Moco.text(value))
  
  def status(code: Int): ResponseHandler = Moco.status(code)
}

