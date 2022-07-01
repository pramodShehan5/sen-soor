package com.pramod.sensoor.actor

import akka.actor.{Actor, Props}
import akka.stream.Materializer
import com.pramod.sensoor.model.{SensorRecord, SensorRecordDetail, SensorResult}

import scala.collection.mutable.HashMap

/**
 * This is for actor handle all the unique sensor details
 * Here we are maintaining map with unique elements.
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

object SensorActor {
  def props()= Props(new SensorActor())
}

class SensorActor extends Actor {
  /** *
   * Here we are using mutable Hashmap. Hashmap is non synchronized.
   * This is not an issue for concurrency.
   * Actor is handling status with concurrently.
   */
  private val sensorMap = HashMap[String, SensorRecordDetail]()


  override def receive: Receive = {
    case record: SensorRecord =>
      println(s"Receive sensor record $record")

      /** *
       * If sensor is already existing in Map, updating existing Map record.
       * If sensor is not in Map, adding new record into Map.
       */
      if (!sensorMap.contains(record.sensorId)) {
        sensorMap += (record.sensorId -> SensorRecord(record))
      } else {
        val existingRecord = sensorMap.get(record.sensorId).head
        sensorMap(record.sensorId) = SensorRecord(record, existingRecord)
      }
      sender ! SensorResult(sensorMap)
  }
}