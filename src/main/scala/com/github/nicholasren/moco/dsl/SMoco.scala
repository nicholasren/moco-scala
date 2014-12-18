package com.github.nicholasren.moco.dsl

import com.github.dreamhead.moco.resource.Resource
import com.github.dreamhead.moco.{MocoConfig, ResponseHandler, RequestMatcher, Moco}
import com.github.dreamhead.moco.handler.{SequenceContentHandler, AndResponseHandler}
import com.github.nicholasren.moco.wrapper.{Rule, PartialRule, ExtractorMatcher}
import scala.collection.JavaConversions._
import com.github.dreamhead.moco.internal.{MocoHttpServer, ActualHttpServer}
import com.github.dreamhead.moco.extractor.{FormRequestExtractor, ContentRequestExtractor, UriRequestExtractor}
import com.google.common.collect.ImmutableList
import Conversions._
import com.github.dreamhead.moco.config.{MocoContextConfig, MocoFileRootConfig}
import com.github.dreamhead.moco.handler.proxy.ProxyConfig
import com.github.dreamhead.moco.handler.failover.Failover
import io.netty.handler.codec.http.HttpResponseStatus
import com.google.common.net.HttpHeaders
import scala.concurrent.duration.Duration
import com.github.dreamhead.moco.procedure.LatencyProcedure

object SMoco {



  //configs
  def fileRoot(path: String): MocoConfig[_] = new MocoFileRootConfig(path)

  def context(context: String): MocoConfig[_] = new MocoContextConfig(context)

  //server
  def server(port: Int): SMoco = new SMoco(port)

  //resources - start
  def uri(value: String): Resource = Moco.uri(value)

  def method(value: String): Resource = Moco.method(value)

  def matched(value: Resource) = Moco.`match`(value)

  def text(value: String): Resource = Moco.text(value)

  def file(filename: String): Resource = Moco.file(filename)

  def version(value: String): Resource = Moco.version(value)
  //resources - end

  //extractor matcher start
  def header(name: String): ExtractorMatcher = new ExtractorMatcher(Moco.header(name))

  def query(name: String): ExtractorMatcher = new ExtractorMatcher(Moco.query(name))

  def cookie(name: String): ExtractorMatcher = new ExtractorMatcher(Moco.cookie(name))

  def uri: ExtractorMatcher = new ExtractorMatcher(new UriRequestExtractor)

  def text: ExtractorMatcher = new ExtractorMatcher(new ContentRequestExtractor)

  def form(key: String): ExtractorMatcher = new ExtractorMatcher(Moco.form(key))

  def xpath(path: String): ExtractorMatcher = new ExtractorMatcher(Moco.xpath(path))

  def jsonPath(path: String): ExtractorMatcher = new ExtractorMatcher(Moco.jsonPath(path))
  //extractor matcher end

  //request matcher start
  def xml(content: Resource): RequestMatcher = Moco.xml(content)

  def json(content: Resource): RequestMatcher = Moco.json(content)
  //request matcher end

  // procedure
  def latency(duration: Duration): LatencyProcedure = Moco.latency(duration.toMillis)

  //response handlers - start
  def status(code: Int): ResponseHandler = Moco.status(code)

  def attachment(filename: String, resource: Resource) = Moco.attachment(filename, resource)

  def redirectTo(uri: String): ResponseHandler = status(HttpResponseStatus.FOUND.code()) and headers(HttpHeaders.LOCATION -> uri)

  def seq(resources: Resource*): ResponseHandler = {
    val handlers = ImmutableList.
      builder[ResponseHandler].
      addAll(resources.map(toHandler)).
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
  //response handlers - end

  //proxy - start
  def proxy(url: String)(implicit failover : Failover) = Moco.proxy(url, failover)

  def proxy(config: => ProxyConfig) = Moco.proxy(config)

  def failover(filename: String): Failover = Moco.failover(filename)

  def playback(filename: String): Failover = Moco.playback(filename)

  def from(localBase: String) = Moco.from(localBase)
  //proxy - end

}


class SMoco(port: Int = 8080) {

  var confs: Seq[MocoConfig[_]] = Seq()

  var rules: List[Rule] = List()

  def running(testFun: => Unit) = {

    val theServer = startServer
    try {
      testFun
    } finally {
      theServer.stop
    }
  }

  def when(matcher: RequestMatcher): PartialRule = new PartialRule(matcher, this)

  def default(handler: ResponseHandler): SMoco = {
    this.rules = Rule.default(handler) :: this.rules
    this
  }

  def configs(configsFun: => CompositeMocoConfig) {
    this.confs = configsFun.items
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
    val server = confs match {
      case confs: Seq[MocoConfig[_]] => com.github.dreamhead.moco.Moco.httpserver(port, confs: _*).asInstanceOf[ActualHttpServer]
      case _ => com.github.dreamhead.moco.Moco.httpserver(port).asInstanceOf[ActualHttpServer]
    }

    rules.foreach {
      rule: Rule =>
        rule.matcher match {
          case Some(matcher) => server.request(matcher).response(rule.handler)
          case None => server.response(rule.handler)
        }
    }
    server
  }

}
