package com.github.nicholasren.moco.wrapper

import com.github.dreamhead.moco.internal.{ActualHttpServer, MocoHttpServer}
import com.github.dreamhead.moco.matcher.AndRequestMatcher
import com.github.dreamhead.moco.handler.AndResponseHandler

import scala.collection.JavaConversions._
class ServerSetting(port: Int) {

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
    server.getSettings.clear()
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


