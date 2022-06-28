package com.pramod.sensoor.controller

import com.pramod.sensoor.model.{SensorRecordDetail, SensorResult}

/**
 * This is using to handle all the final output
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

object RecordController {
  def printFinalResult(result: SensorResult): Unit = {
//    println(result)
    println(s"Num of processed measurements  ${result.getTotalProcessedRecords}")
    println(s"Num of failed measurements ${result.getFailedRecordCount}")
    println(s"Sensors with highest avg humidity:")
    println(s"sensor-id => min, avg, max")
    result.resultMap.toList.filter(r => r._2.count != r._2.invalidCnt).sortWith(sortByAverage).foreach(record => {
      val sensRec = record._2
      println(s"${record._1} => ${sensRec.min} , ${sensRec.total/(sensRec.count - sensRec.invalidCnt)}  , ${sensRec.max}")
    })
    result.resultMap.toList.filter(r => r._2.count == r._2.invalidCnt).sortWith(sortByAverage).foreach(record => {
      println(s"${record._1} => NaN, NaN, NaN")
    })
  }

  def sortByAverage(s1: (String, SensorRecordDetail), s2: (String, SensorRecordDetail)): Boolean = {
    //    println("Comparing values %s and %s".format(s1, s2))
    (s1._2.total / (s1._2.count - s1._2.invalidCnt)) > (s2._2.total / (s2._2.count - s2._2.invalidCnt))
  }
}