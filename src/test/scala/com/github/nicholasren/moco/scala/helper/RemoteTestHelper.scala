package org.github.nicholasren.moco.scala.helper

import org.apache.http.client.fluent.Request
import scala.Predef.String
import java.lang.String
import scala.Predef.String
import org.apache.http.message.BasicNameValuePair

object RemoteTestHelper {

  def defaultPort = 8080

  def sendGet(uri: String): String = {
    Request.Get(uri).execute().returnContent().asString
  }

  def sendPost(uri: String, content: String): String = {
    postBytes(uri, content.getBytes())
  }

  def doPut(uri: String) = {
    Request.Put(uri).execute().returnContent().toString()
  }

  def delete(uri: String) = {
    Request.Delete(uri).execute().returnContent().toString()
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

  def requestWithHeader(name: String, value: String) = {
    Request.Get(root).addHeader(name, value).execute().returnContent().asString
  }

  def requestWithCookie(name: String, value: String) = {
    Request.Post(root)
  }

  def postForm(name: String, value: String): String = {
    Request.Post(root).bodyForm(new BasicNameValuePair(name, value)).execute().returnContent().asString()
  }


  def root: String = {
    "http://localhost:" + defaultPort
  }

  def remoteUrl(uri: String) = root + uri
}