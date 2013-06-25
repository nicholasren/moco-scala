package org.github.nicholasren.moco.scala

import com.github.dreamhead.moco.HttpServer
import com.github.dreamhead.moco.internal.{ActualHttpServer, MocoHttpServer}

object MocoScala {
  def running(httpServer: HttpServer, assertion: () => Unit) = {
    val server = new MocoHttpServer(httpServer.asInstanceOf[ActualHttpServer])
    try {
      server.start()
      assertion.apply()
    }
    finally server.stop()
  }
}
