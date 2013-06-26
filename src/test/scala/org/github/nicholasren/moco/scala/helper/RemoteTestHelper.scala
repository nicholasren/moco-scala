package org.github.nicholasren.moco.scala.helper

import org.apache.http.client.fluent.Request

object RemoteTestHelper {

  def port = 8080

  def get(uri: String): String = {
    Request.Get(uri).execute().returnContent().asString
  }

  def post(uri: String, content: String): String = {
    postBytes(uri, content.getBytes())
  }

  def postBytes(uri: String, bytes: Array[Byte]) = {
    val content = Request.Post(uri).bodyByteArray(bytes)
      .execute().returnContent();
    content.asString();
  }


  def root: String = {
    "http://localhost:" + port
  }

  def remoteUrl(uri: String) = root + uri
}
