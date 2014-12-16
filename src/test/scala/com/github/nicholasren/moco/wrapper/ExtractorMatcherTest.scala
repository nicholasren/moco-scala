package com.github.nicholasren.moco.wrapper

import org.scalatest.{Matchers, FlatSpec}
import com.github.dreamhead.moco.extractor.PlainTextExtractor
import Matchers._
import com.github.dreamhead.moco.matcher.{EndsWithMatcher, StartsWithMatcher, ContainMatcher, EqRequestMatcher}

class ExtractorMatcherTest extends FlatSpec {
  val extractor: PlainTextExtractor = new PlainTextExtractor("hello world")
  val matcher: ExtractorMatcher = new ExtractorMatcher(extractor)

  "a extractor matcher" should "be able to do exact match" in {
      (matcher === "hello") shouldBe a [EqRequestMatcher[String]]
  }

  "a extractor matcher" should "be able to do contain match" in {
    (matcher contain "wor") shouldBe a [ContainMatcher[String]]
  }

  "a extractor matcher" should "be able to do startsWith match" in {
    (matcher startsWith "hell") shouldBe a [StartsWithMatcher[String]]
  }

  "a extractor matcher" should "be able to do endsWith match" in {
    (matcher endsWith "hell") shouldBe a [EndsWithMatcher[String]]
  }

}
