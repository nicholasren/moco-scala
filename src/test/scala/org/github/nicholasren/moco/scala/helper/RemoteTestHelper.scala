package org.github.nicholasren.moco.scala.helper

import org.apache.http.client.fluent.Request

object RemoteTestHelper {

  def port = 8080

  def get(uri: String): String = {
    Request.Get(uri).execute().returnContent().asString
  }

  def root: String = {
    "http://localhost:" + port
  }
}
