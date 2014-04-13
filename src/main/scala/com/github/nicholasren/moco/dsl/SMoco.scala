package com.github.nicholasren.moco.dsl

import com.github.dreamhead.moco.resource.Resource
import com.github.dreamhead.moco.{ResponseHandler, RequestMatcher, Moco}
import com.github.dreamhead.moco.handler.{SequenceContentHandler, AndResponseHandler}
import com.github.nicholasren.moco.wrapper.{ServerSetting, PartialRule, ExtractorMatcher}
import scala.collection.JavaConversions._

object SMoco {

  //server
  def server(port: Int): ServerSetting = new ServerSetting(port)

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

  //ExtractorMatcher
  def header(name: String): ExtractorMatcher = new ExtractorMatcher(Moco.header(name))

  def query(name: String): ExtractorMatcher = new ExtractorMatcher(Moco.query(name))

  def cookie(name: String): ExtractorMatcher = new ExtractorMatcher(Moco.cookie(name))

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