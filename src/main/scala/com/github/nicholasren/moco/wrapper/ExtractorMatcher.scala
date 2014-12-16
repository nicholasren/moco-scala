package com.github.nicholasren.moco.wrapper

import com.github.dreamhead.moco.{RequestMatcher, Moco, RequestExtractor}
import com.github.dreamhead.moco.resource.Resource


case class ExtractorMatcher(extractor: RequestExtractor[_ <: Any]) {
  def ===(expected: String): RequestMatcher = Moco.eq(extractor, expected)

  def ===(expected: Resource): RequestMatcher = Moco.eq(extractor, expected)

  def matched(expected: String): RequestMatcher = Moco.`match`(extractor, expected)

  def contain(expected: String): RequestMatcher = Moco.contain(extractor, expected)

  def startsWith(prefix: String): RequestMatcher = Moco.startsWith(extractor, prefix)

  def endsWith(suffix: String): RequestMatcher = Moco.endsWith(extractor, suffix)
}
