package com.pramod.sensoor.util

import com.pramod.sensoor.exception.SensorException
import com.pramod.sensoor.model.SensorRecord
import com.pramod.sensoor.util.ObjectUtil._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.{convertToAnyShouldWrapper, equal}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Try}

class ObjectUtilTest extends AnyFlatSpec {
  "ObjectUtil convertTryToFuture" should "return success future when providing success" in {
    Await.result(Try(SensorRecord("s1", "23")).convertTryToFuture, Duration.Inf) should equal(SensorRecord("s1", "23"))
  }

  it should "return Failure future when providing failure" in {
    Try(throw new SensorException("test")).convertTryToFuture.onComplete {
      case Failure(e) => succeed
      case _ => fail("fail here")
    }
  }
}