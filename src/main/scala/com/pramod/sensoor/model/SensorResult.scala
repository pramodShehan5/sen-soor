package com.pramod.sensoor.model

import scala.collection.mutable.HashMap
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * This is the final output
 * here we are using only map
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

case class SensorResult(resultMap: HashMap[String, SensorRecordDetail] = HashMap.empty[String, SensorRecordDetail]) {
  def getFutureResult(result: SensorResult): Future[SensorResult] = {
    Future {
      result
    }
  }

  /***
   * here we are calculating all the sensors count.
   * @return
   */
  def getTotalProcessedRecords: Int = {
    resultMap.foldLeft(0)((fValue: Int, record: (String, SensorRecordDetail)) => fValue + record._2.count)
  }

  /***
   * here we are calculating all the failed records count.
   * @return
   */
  def getFailedRecordCount: Int = {
    resultMap.foldLeft(0)((fValue: Int, record: (String, SensorRecordDetail)) => fValue + record._2.invalidCnt)
  }
}