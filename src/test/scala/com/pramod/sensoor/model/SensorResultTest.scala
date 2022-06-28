package com.pramod.sensoor.model

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.{convertToAnyShouldWrapper, equal}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class SensorResultTest extends AnyFlatSpec {
  "SensorResult getFutureResult" should "return future result" in {
    val resultMap: scala.collection.mutable.HashMap[String, SensorRecordDetail] = scala.collection.mutable.HashMap("s1" -> SensorRecordDetail(10, 20, 100, 2, 1))
    val sensorResult = SensorResult(resultMap)
    Await.result(sensorResult.getFutureResult(sensorResult), Duration.Inf) should equal(sensorResult)
  }

  "SensorResult getTotalProcessedRecords" should "return total of count and invalid records" in {
    val resultMap: scala.collection.mutable.HashMap[String, SensorRecordDetail] = scala.collection.mutable.HashMap("s1" -> SensorRecordDetail(10, 20, 100, 2, 1),
      "s2" -> SensorRecordDetail(11, 14, 54, 3, 1))
    val sensorResult = SensorResult(resultMap)
    sensorResult.getTotalProcessedRecords should equal(5)
  }

  it should "return total of count and invalid records when NaN records in the map" in {
    val resultMap: scala.collection.mutable.HashMap[String, SensorRecordDetail] = scala.collection.mutable.HashMap("s1" -> SensorRecordDetail(10, 20, 100, 2, 1),
      "s2" -> SensorRecordDetail(11, 14, 54, 3, 1), "s3" -> SensorRecordDetail(0, 0, 0, 3, 3))
    val sensorResult = SensorResult(resultMap)
    sensorResult.getTotalProcessedRecords should equal(8)
  }

  "SensorResult getFailedRecordCount" should "return  invalid records total" in {
    val resultMap: scala.collection.mutable.HashMap[String, SensorRecordDetail] = scala.collection.mutable.HashMap("s1" -> SensorRecordDetail(10, 20, 100, 2, 1),
      "s2" -> SensorRecordDetail(11, 14, 54, 3, 1))
    val sensorResult = SensorResult(resultMap)
    sensorResult.getFailedRecordCount should equal(2)
  }

  it should "return  invalid records total when NaN records in the map" in {
    val resultMap: scala.collection.mutable.HashMap[String, SensorRecordDetail] = scala.collection.mutable.HashMap("s1" -> SensorRecordDetail(10, 20, 100, 2, 1),
      "s2" -> SensorRecordDetail(11, 14, 54, 3, 1), "s3" -> SensorRecordDetail(0, 0, 0, 2, 2))
    val sensorResult = SensorResult(resultMap)
    sensorResult.getFailedRecordCount should equal(4)
  }
}