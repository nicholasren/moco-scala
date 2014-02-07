package com.github.nicholasren.moco.scala.dsl

import com.github.dreamhead.moco.{ResponseHandler, Moco, RequestMatcher}
import com.github.dreamhead.moco.internal.{ActualHttpServer, MocoHttpServer}
import com.github.dreamhead.moco.resource.{TextResource, Resource}
import com.github.dreamhead.moco.handler.ResponseHandlers._
import scala.Option

class SMoco(port: Int) {
  type Rule = (Option[RequestMatcher], ResponseHandler)

  val server = com.github.dreamhead.moco.Moco.httpserver(port)

  def record(rules: Rule*) {
    rules.foreach(rule => {
      val (matcher, responseHandler) = rule
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
}

object SMoco {

  implicit def stringToResource(string: String): Resource = Moco.text(string)

  implicit def enrich(origin: RequestMatcher) = new RichRequestMatcher(origin)

  def server(port: Int) = {
    new SMoco(port)
  }

  def running(moco: SMoco)(testFun: => Unit) = {
    val server = new MocoHttpServer(moco.server.asInstanceOf[ActualHttpServer])
    try {
      server.start
      testFun
    }
    finally server.stop
  }

  def when(matcher: RequestMatcher) = new When(Some(matcher))

  def default(resource: Resource) = whenDefault.then(responseHandler(resource))

  def default(handler: ResponseHandler) = whenDefault.then(handler)

  def post(matcher: RequestMatcher) = new When(Some(enrich(matcher) and method -> "post"))

  def get(theUri: String) = new When(Some(enrich(uri -> theUri) and method -> "get"))

  //recourse related dsl
  def file(name: String) = Moco.file(name)

  def seq(values: String*) = Moco.seq(values.map(Moco.text(_)): _*)

  //request matcher builder
  def uri = new RequestMatcherBuilder {

    def ->(value: String): RequestMatcher = Moco.by(Moco.uri(value))
  }

  def method = new RequestMatcherBuilder {
    def ->(value: String): RequestMatcher = Moco.by(Moco.method(value))
  }

  def pathResource = new RequestMatcherBuilder {
    def ->(value: String): RequestMatcher = Moco.by(Moco.pathResource(value))
  }

  def content = new RequestMatcherBuilder {
    def ->(value: String): RequestMatcher = Moco.by(Moco.text(value))
  }

  def param(name: String) = new RequestMatcherBuilder {
    def ->(value: String): RequestMatcher = Moco.eq(Moco.query(name), value)
  }

  def version = new RequestMatcherBuilder {
    def ->(value: String): RequestMatcher = Moco.by(Moco.version(value))
  }

  def header(name: String) = new RequestMatcherBuilder {
    def ->(value: String): RequestMatcher = Moco.eq(Moco.header(name), value)
  }

  def cookie(name: String) = new RequestMatcherBuilder {
    def ->(value: String): RequestMatcher = ???
  }

  def form(name: String) = new RequestMatcherBuilder {
    def ->(value: String): RequestMatcher = Moco.eq(Moco.form(name), value)
  }

  private
  def whenDefault = new When(None)
}

