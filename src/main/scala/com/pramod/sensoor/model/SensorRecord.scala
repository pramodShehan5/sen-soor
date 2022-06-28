package com.pramod.sensoor.model

import com.pramod.sensoor.util.Constants

/**
 * This is the record to keep sensorId and sensorValue
 * Here we are calculating all the min, max, total and etc.
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

case class SensorRecord(sensorId: String, sensorValue: String) {
  private def calculateTotal(existingRecord: SensorRecordDetail): Double = {
    existingRecord.total + sensorValue.toDouble
  }

  private def calculateMax(existingRecord: SensorRecordDetail): Double = {
    if (existingRecord.max >= sensorValue.toDouble) {
      existingRecord.max
    } else {
      sensorValue.toDouble
    }
  }

  /**
   * Here default value is 0.0. when first time value is NAN, default value is 0.0.
   * So every time 0.0 is min value. that is why here checking the count == invalidcount
   * @param existingRecord
   * @return
   */
  private def calculateMin(existingRecord: SensorRecordDetail): Double = {
    if (existingRecord.count == existingRecord.invalidCnt) {
      sensorValue.toDouble
    } else {
      if (existingRecord.min <= sensorValue.toDouble) {
        existingRecord.min
      } else {
        sensorValue.toDouble
      }
    }
  }

  private def calculateRecordCount(existingRecord: SensorRecordDetail): Int = {
    /*if (sensorValue.equalsIgnoreCase("Nan")) {
      existingRecord.count + 1
    } else {
      existingRecord.count + 1
    }*/
    existingRecord.count + 1
  }

  private def calculateInvalidRecordCount(existingRecord: SensorRecordDetail): Int = {
    if (sensorValue.equalsIgnoreCase(Constants.NAN)) {
      existingRecord.invalidCnt + 1
    } else {
      existingRecord.invalidCnt
    }
  }
}

object SensorRecord {
  def apply(sensorRecord: SensorRecord, existingRecord: SensorRecordDetail): SensorRecordDetail = {
    sensorRecord.sensorValue match {
      case Constants.NAN =>
        existingRecord.copy(count = existingRecord.count + 1, invalidCnt = existingRecord.invalidCnt + 1)
      case _ =>
        SensorRecordDetail(sensorRecord.calculateMin(existingRecord),
          sensorRecord.calculateMax(existingRecord),
          sensorRecord.calculateTotal(existingRecord),
          sensorRecord.calculateRecordCount(existingRecord),
          sensorRecord.calculateInvalidRecordCount(existingRecord))
    }
  }

  def apply(sensorRecord: SensorRecord): SensorRecordDetail = {
    sensorRecord.sensorValue match {
      case Constants.NAN =>
        SensorRecordDetail(count = 1, invalidCnt = 1)
      case _ =>
        val senValue = sensorRecord.sensorValue.toInt
        SensorRecordDetail(senValue, senValue, senValue, 1)
    }
  }
}