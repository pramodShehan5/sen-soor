package com.pramod.sensoor.actor

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.pramod.sensoor.model.{SensorRecord, SensorRecordDetail, SensorResult}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.collection.mutable

class SensorActorTest extends TestKit(ActorSystem("sensoor"))
  with ImplicitSender
  with AnyWordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }
  "SensorActor" must {

    "send back Sensor Result with min, max and total when submit correct record" in {
      val echo = system.actorOf(SensorActor.props())
      echo ! SensorRecord("s1", "20")
      expectMsg( SensorResult(mutable.HashMap("s1" -> SensorRecordDetail(20.0,20.0,20.0,1,0))))
    }

    "send back Sensor Result with min, max and total when submit incorrect record" in {
      val echo = system.actorOf(SensorActor.props())
      echo ! SensorRecord("s1", "NaN")
      expectMsg( SensorResult(mutable.HashMap("s1" -> SensorRecordDetail(0.0,0.0,0.0,1,1))))
    }

    "send back Sensor Result with min, max and total when submit incorrect record and correct record" in {
      val echo = system.actorOf(SensorActor.props())
      echo ! SensorRecord("s1", "NaN")
      echo ! SensorRecord("s1", "12")
      Thread.sleep(1000)
      expectMsg( SensorResult(mutable.HashMap("s1" -> SensorRecordDetail(12.0,12.0,12.0,2,1))))
    }

    "send back Sensor Result with min, max and total when submit incorrect record and correct record with different sensor ids" in {
      val echo = system.actorOf(SensorActor.props())
      echo ! SensorRecord("s1", "NaN")
      echo ! SensorRecord("s2", "15")
      echo ! SensorRecord("s1", "12")
      echo ! SensorRecord("s1", "32")
      echo ! SensorRecord("s2", "14")
      echo ! SensorRecord("s3", "12")
      echo ! SensorRecord("s3", "NaN")
      echo ! SensorRecord("s4", "NaN")
      Thread.sleep(1000)
      val result = SensorResult(mutable.HashMap("s3" -> SensorRecordDetail(12.0,12.0,12.0,2,1),
        "s4" -> SensorRecordDetail(0.0,0.0,0.0,1,1), "s1" -> SensorRecordDetail(12.0,32.0,44.0,3,1),
        "s2" -> SensorRecordDetail(14.0,15.0,29.0,2,0)))
      expectMsg(result)
      result.resultMap("s1").max should be(32.0)
      result.resultMap("s1").min should be(12.0)
      result.resultMap("s1").total should be(44.0)
      result.resultMap("s1").count should be(3)
      result.resultMap("s1").invalidCnt should be(1)

      result.resultMap("s2").max should be(15.0)
      result.resultMap("s2").min should be(14.0)
      result.resultMap("s2").total should be(29.0)
      result.resultMap("s2").count should be(2)
      result.resultMap("s2").invalidCnt should be(0)

      result.resultMap("s3").max should be(12.0)
      result.resultMap("s3").min should be(12.0)
      result.resultMap("s3").total should be(12.0)
      result.resultMap("s3").count should be(2)
      result.resultMap("s3").invalidCnt should be(1)

      result.resultMap("s4").max should be(0.0)
      result.resultMap("s4").min should be(0.0)
      result.resultMap("s4").total should be(0.0)
      result.resultMap("s4").count should be(1)
      result.resultMap("s4").invalidCnt should be(1)
    }
  }
}