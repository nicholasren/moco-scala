package com.github.nicholasren.moco.dsl

import com.github.dreamhead.moco.resource.Resource
import com.github.dreamhead.moco._
import com.github.dreamhead.moco.handler.AndResponseHandler
import com.github.dreamhead.moco.matcher.AndRequestMatcher
import scala.collection.JavaConverters._
import com.github.dreamhead.moco.handler.failover.Failover

object Conversions {
  //implicit
  implicit val failover: Failover = Failover.DEFAULT_FAILOVER

  implicit def toResource(text: String): Resource = Moco.text(text)

  implicit def toMatcher(resource: Resource): RequestMatcher = Moco.`match`(resource)

  implicit def toHandler(resource: Resource): ResponseHandler = Moco.`with`(resource)

  implicit def toHandler(procedure: MocoProcedure): ResponseHandler = Moco.`with`(procedure)

  implicit def toCompositeMocoConfig(config: MocoConfig[_]) = new CompositeMocoConfig(Seq(config))

  case class CompositeMocoConfig(items: Seq[MocoConfig[_]]) {
    def and(config: MocoConfig[_]): CompositeMocoConfig = new CompositeMocoConfig(config +: items)
  }

  implicit class RichResource(target: Resource) {
    def and(handler: ResponseHandler): ResponseHandler = new AndResponseHandler(Seq[ResponseHandler](handler, target).asJava)

    def and(matcher: RequestMatcher): RequestMatcher = new AndRequestMatcher(Seq[RequestMatcher](matcher, target).asJava)

    def and(resource: Resource): RequestMatcher = new AndRequestMatcher(Seq[RequestMatcher](resource, target).asJava)
  }

  implicit class RichRequestMatcher(target: RequestMatcher) {
    def and(matcher: RequestMatcher): RequestMatcher = new AndRequestMatcher(Seq(matcher, target).asJava)
  }

  implicit class RichResponseHandler(target: ResponseHandler) {
    def and(handler: ResponseHandler): ResponseHandler = new AndResponseHandler(Seq(handler, target).asJava)
  }

}
