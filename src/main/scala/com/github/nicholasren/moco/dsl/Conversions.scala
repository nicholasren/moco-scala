package com.github.nicholasren.moco.dsl

import com.github.dreamhead.moco.resource.Resource
import com.github.dreamhead.moco.{MocoConfig, ResponseHandler, RequestMatcher, Moco}
import com.github.dreamhead.moco.handler.AndResponseHandler
import com.github.dreamhead.moco.matcher.AndRequestMatcher
import scala.collection.JavaConversions._
import com.github.dreamhead.moco.handler.failover.Failover

object Conversions {
  //implicit
  implicit def toResource(text: String): Resource = Moco.text(text)

  implicit def toMatcher(resource: Resource): RequestMatcher = Moco.`match`(resource)

  implicit def toHandler(resource: Resource): ResponseHandler = Moco.`with`(resource)

  implicit def toCompositeMocoConfig(config: MocoConfig[_]) = new CompositeMocoConfig(Seq(config))

  implicit val failover: Failover = Failover.DEFAULT_FAILOVER

  case class CompositeMocoConfig(items: Seq[MocoConfig[_]]) {
    def and(config: MocoConfig[_]): CompositeMocoConfig = new CompositeMocoConfig(config +: items )
  }

  implicit class RichResource(target: Resource) {
    def and(handler: ResponseHandler): ResponseHandler = new AndResponseHandler(Seq[ResponseHandler](handler, target))

    def and(matcher: RequestMatcher): RequestMatcher = new AndRequestMatcher(Seq[RequestMatcher](matcher, target))

    def and(resource: Resource): RequestMatcher = new AndRequestMatcher(Seq[RequestMatcher](resource, target))
  }

  implicit class RichRequestMatcher(target: RequestMatcher) {
    def and(matcher: RequestMatcher): RequestMatcher = new AndRequestMatcher(Seq(matcher, target))
  }

  implicit class RichResponseHandler(target: ResponseHandler) {
    def and(handler: ResponseHandler): ResponseHandler = new AndResponseHandler(Seq(handler, target))
  }
}
