package com.github.nicholasren.moco.dsl

import com.github.dreamhead.moco.resource.Resource
import com.github.dreamhead.moco.{MocoConfig, ResponseHandler, RequestMatcher, Moco}
import com.github.dreamhead.moco.handler.{SequenceContentHandler, AndResponseHandler}
import com.github.nicholasren.moco.wrapper.{Rule, PartialRule, ExtractorMatcher}
import scala.collection.JavaConversions._
import com.github.dreamhead.moco.internal.{MocoHttpServer, ActualHttpServer}
import com.github.dreamhead.moco.matcher.AndRequestMatcher
import com.github.dreamhead.moco.extractor.{VersionExtractor, ContentRequestExtractor, UriRequestExtractor}

object SMoco {

  //server
  def server(port: Int): SMoco = new SMoco(port)

  def when(conditions: Any*): PartialRule = {
    val matchers = conditions.map {
      _ match {
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

  def file(filename: String): Resource = Moco.file(filename)

  def version(value: String): Resource = Moco.version(value)

  //extractor matcher
  def header(name: String): ExtractorMatcher = new ExtractorMatcher(Moco.header(name))

  def query(name: String): ExtractorMatcher = new ExtractorMatcher(Moco.query(name))

  def cookie(name: String): ExtractorMatcher = new ExtractorMatcher(Moco.cookie(name))

  def uri: ExtractorMatcher = new ExtractorMatcher(new UriRequestExtractor)

  def text: ExtractorMatcher = new ExtractorMatcher(new ContentRequestExtractor)



  //handlers
  def status(code: Int): ResponseHandler = Moco.status(code)

  def seq(resources: Resource*): ResponseHandler = {
    val handlers = resources.map { resource => Moco.`with`(resource)}
    new SequenceContentHandler(handlers.toArray)
  }

  def headers(headers: (String, String)*): ResponseHandler = {
    val handlers = headers.map {
      header =>
        Moco.header(header._1, header._2)
    }
    new AndResponseHandler(handlers)
  }

  def cookies(cookies: (String, String)*): ResponseHandler = {
    val handlers = cookies.map {
      cookie =>
        Moco.cookie(cookie._1, cookie._2)
    }
    new AndResponseHandler(handlers)
  }
}


class SMoco(port: Int) {

  val server: ActualHttpServer = com.github.dreamhead.moco.Moco.httpserver(port).asInstanceOf[ActualHttpServer]

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
