package org.github.nicholasren.moco.scala.helper

import org.apache.http.client.fluent.Request

object RemoteTestHelper {

  def defaultPort = 8080

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

  def statusCode(method: String, uri: String) = {

    val request = method match {
      case "GET" => Request.Get(uri)
      case "POST" => Request.Post(uri)
    }

    request.execute().returnResponse().getStatusLine.getStatusCode
  }


  def root: String = {
    "http://localhost:" + defaultPort
  }

  def remoteUrl(uri: String) = root + uri
}
