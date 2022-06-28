package com.pramod.sensoor.model

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.{convertToAnyShouldWrapper, equal}

class SensorRecordTest extends AnyFlatSpec {
  "SensorRecord" should "return SensorRecordDetail when existing that sensor in map and value max is greater than" +
    " existing max value" in {
    val sensorRecord = SensorRecord(SensorRecord("s1", "28"), SensorRecordDetail(11, 14, 54, 3, 1))
    sensorRecord should equal(SensorRecordDetail(11, 28, 82, 4, 1))
  }

  it should "return SensorRecordDetail when existing that sensor in map and value is NaN" in {
    val sensorRecord = SensorRecord(SensorRecord("s1", "NaN"), SensorRecordDetail(11, 14, 54, 3, 1))
    sensorRecord should equal(SensorRecordDetail(11, 14, 54, 3, 2))
  }

  it should "return SensorRecordDetail when existing that sensor in map and value is min than existing min value" in {
    val sensorRecord = SensorRecord(SensorRecord("s1", "2"), SensorRecordDetail(11, 14, 54, 3, 1))
    sensorRecord should equal(SensorRecordDetail(2, 14, 56, 4, 1))
  }

  "SensorRecord" should "return SensorRecord when not existing that sensor in map and value is NaN" in {
    val sensorRecord = SensorRecord(SensorRecord("s1", "NaN"))
    sensorRecord should equal(SensorRecordDetail(0, 0, 0, 1, 1))
  }

  it should "return SensorRecord when not existing that sensor in map " in {
    val sensorRecord = SensorRecord(SensorRecord("s1", "12"))
    sensorRecord should equal(SensorRecordDetail(12, 12, 12, 1, 0))
  }
}