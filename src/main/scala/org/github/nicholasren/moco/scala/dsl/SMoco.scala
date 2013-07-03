package org.github.nicholasren.moco.scala.dsl

import com.github.dreamhead.moco.{RequestMatcher, Moco}
import com.github.dreamhead.moco.internal.{ActualHttpServer, MocoHttpServer}
import com.github.dreamhead.moco.handler.ResponseHandlers._
import com.github.dreamhead.moco.extractor.Extractors

class SMoco(port: Int, configBlock: => (RequestMatcher, String)) {
  val server = com.github.dreamhead.moco.Moco.httpserver(port)
  val (m, s) = configBlock
  server.request(m).response(s)
}

trait SRequestMatcher {
  def ->(value: String): RequestMatcher
}

object SRequestMatcher {
  def uri = new SRequestMatcher {
    def ->(value: String): RequestMatcher = {
      val resource = Moco.uri(value)
      Moco.eq(Extractors.extractor(resource.id()), resource)
    }
  }
}

object server {
  def apply(port: Int)(condition: => (RequestMatcher, String)) = {

    new SMoco(port, condition)
  }
}

object when {
  def apply(condition: => RequestMatcher) = new Then {
    def then(content: => String) = (condition, content)
  }
}

trait Then {
  def then(content: => String): (RequestMatcher, String)
}

object SMoco {

  def running(smoco: SMoco)(block: => Unit) = {
    val server = new MocoHttpServer(smoco.server.asInstanceOf[ActualHttpServer])
    try {
      server.start
      block
    }
    finally server.stop
  }


  def file(path: String) = {
    responseHandler(com.github.dreamhead.moco.Moco.file(path))
  }

  def text(text: String) = {
    responseHandler(com.github.dreamhead.moco.Moco.text(text))
  }

  def by(content: String) = {
    com.github.dreamhead.moco.Moco.by(content)
  }
}
