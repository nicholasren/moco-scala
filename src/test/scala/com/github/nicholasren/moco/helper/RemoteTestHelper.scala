package org.github.nicholasren.moco.helper

import org.apache.http.client.fluent.Request
import scala.Predef.String
import java.lang.String
import scala.Predef.String
import org.apache.http.message.BasicNameValuePair
import org.apache.http.HttpVersion

trait RemoteTestHelper {

  val port: Int

  def get(uri: String): String = {
    Request.Get(uri).execute().returnContent().asString
  }

  def post(uri: String, content: String): String = {
    postBytes(uri, content.getBytes())
  }

  def put(uri: String) = {
    Request.Put(uri).execute().returnContent().toString()
  }

  def delete(uri: String) = {
    Request.Delete(uri).execute().returnContent().toString()
  }

  def postBytes(uri: String, bytes: Array[Byte]) = {
    Request.Post(uri).bodyByteArray(bytes)
      .execute().returnContent().asString();
  }

  def getForStatus(uri: String) = {
    Request.Get(uri).execute().returnResponse().getStatusLine.getStatusCode
  }

  def getWithHeaders(headers: (String, String)*) = {
    val get: Request = Request.Get(root)

    headers.foreach { header =>
      get.addHeader(header._1, header._2)
    }

    get.execute().returnContent().asString
  }

  def getForStatusWithHeaders(headers: (String, String)*) = {
    val get: Request = Request.Get(root)

    headers.foreach { header =>
      get.addHeader(header._1, header._2)
    }

    get.execute.returnResponse.getStatusLine.getStatusCode
  }

  def getForHeader(headerName: String): String = {
    Request.Get(root).execute.returnResponse.getFirstHeader(headerName).getValue
  }

  def getWithVersion(url: String, version: HttpVersion) = get(Request.Get(url).version(version))


  def root: String = {
    s"http://localhost:$port"
  }

  def remoteUrl(uri: String) = root + uri

  private
  def get(request: Request): String =  request.execute.returnContent.asString
}
