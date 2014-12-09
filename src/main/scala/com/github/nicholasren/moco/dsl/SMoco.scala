package com.github.nicholasren.moco.dsl

import com.github.dreamhead.moco.resource.Resource
import com.github.dreamhead.moco.{MocoConfig, ResponseHandler, RequestMatcher, Moco}
import com.github.dreamhead.moco.handler.{SequenceContentHandler, AndResponseHandler}
import com.github.nicholasren.moco.wrapper.{Rule, PartialRule, ExtractorMatcher}
import scala.collection.JavaConversions._
import com.github.dreamhead.moco.internal.{MocoHttpServer, ActualHttpServer}
import com.github.dreamhead.moco.extractor.{ContentRequestExtractor, UriRequestExtractor}
import com.google.common.collect.ImmutableList
import Conversions._
import com.github.dreamhead.moco.config.{MocoContextConfig, MocoFileRootConfig}

object SMoco {

  //configs
  def fileRoot(path: String): MocoConfig[_] = new MocoFileRootConfig(path)

  def context(context: String): MocoConfig[_] = new MocoContextConfig(context)

  //server
  def server(port: Int): SMoco = new SMoco(port)

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
    val handlers = ImmutableList.
      builder[ResponseHandler].
      addAll(resources.map (toHandler)).
      build
    new SequenceContentHandler(handlers)
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


class SMoco(port: Int = 8080) {

  var configs: Option[MocoConfig[_]] = None

  var rules: List[Rule] = List[Rule]()

  def running(testFun: => Unit) = {

    val theServer = startServer
    try {
      testFun
    } finally {
      theServer.stop
    }
  }

  def when(matcher: RequestMatcher): PartialRule = new PartialRule(matcher, this)

  def config(configFun: => MocoConfig[_]) {
    this.configs = Some(configFun)
  }

  def record(rule: Rule) = {
    this.rules = rule :: this.rules
  }

  private def startServer = {
    val theServer = new MocoHttpServer(replay)
    theServer.start
    theServer
  }

  private def replay = {
   val server = configs match {
      case Some(conf: MocoConfig[_]) => com.github.dreamhead.moco.Moco.httpserver(port, conf).asInstanceOf[ActualHttpServer]
      case _ => com.github.dreamhead.moco.Moco.httpserver(port).asInstanceOf[ActualHttpServer]
    }

    rules.foreach { rule: Rule => server.request(rule.matcher).response(rule.handler) }
    server
  }

}
