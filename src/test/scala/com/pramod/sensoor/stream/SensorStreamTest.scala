package com.pramod.sensoor.stream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.stream.testkit.scaladsl.TestSink
import com.pramod.sensoor.model.{SensorRecord, SensorRecordDetail, SensorResult}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.File

class SensorStreamTest extends AnyFlatSpec with Matchers {

  implicit val system = ActorSystem("test")
  implicit val materializer = ActorMaterializer()

  "SensorStream parseFile" should "get all the lines as sensorRecords" in {
    val tested = new SensorStream().parseFile
    val cassalogDirectory = getClass.getClassLoader.getResource("sensor")
    val file = new File(cassalogDirectory.toURI)
    file.listFiles().toList
    val readings = file.listFiles().toList
    val flow = Source(readings).via(tested)
    flow.runWith(TestSink.probe[SensorRecord])
      .request(5)
      .expectNextUnordered(SensorRecord("s1", "1"), SensorRecord("s2", "5"), SensorRecord("s4", "NaN"), SensorRecord("s5", "50"),
        SensorRecord("s1", "12"))
      .expectComplete()
  }

  "SensorStream createSensorRecordFlow" should "return SensorResult when given different sensor records" in {
    val tested = new SensorStream().createSensorRecordFlow
    val readings = List(SensorRecord("s1", "1"), SensorRecord("s2", "5"))
    val res1 = SensorResult(scala.collection.mutable.HashMap("s1" -> SensorRecordDetail(1, 1, 1, 1, 0),
      "s2" -> SensorRecordDetail(5, 5, 5, 1, 0)))

    val res2 = SensorResult(scala.collection.mutable.HashMap("s1" -> SensorRecordDetail(1, 1, 1, 1, 0),
      "s2" -> SensorRecordDetail(5, 5, 5, 1, 0)))
    val flow = Source(readings).via(tested)
    flow.runWith(TestSink.probe[SensorResult])
      .request(2)
      .expectNextUnordered(res1, res2)
      .expectComplete()
  }

  it should "return SensorResult when given different sensor records with NaN" in {
    val tested = new SensorStream().createSensorRecordFlow
    val readings = List(SensorRecord("s1", "1"), SensorRecord("s2", "5"), SensorRecord("s3", "NaN"))
    val res1 = SensorResult(scala.collection.mutable.HashMap("s3" -> SensorRecordDetail(0, 0, 0, 1, 1),
      "s1" -> SensorRecordDetail(1, 1, 1, 1, 0), "s2" -> SensorRecordDetail(5, 5, 5, 1, 0)))
    val res2 = SensorResult(scala.collection.mutable.HashMap("s3" -> SensorRecordDetail(0, 0, 0, 1, 1),
      "s1" -> SensorRecordDetail(1, 1, 1, 1, 0), "s2" -> SensorRecordDetail(5, 5, 5, 1, 0)))
    val res3 = SensorResult(scala.collection.mutable.HashMap("s3" -> SensorRecordDetail(0, 0, 0, 1, 1),
      "s1" -> SensorRecordDetail(1, 1, 1, 1, 0), "s2" -> SensorRecordDetail(5, 5, 5, 1, 0)))
    val flow = Source(readings).via(tested)
    flow.runWith(TestSink.probe[SensorResult])
      .request(3)
      .expectNextUnordered(res3, res1, res2)
      .expectComplete()
  }

  it should "return SensorResult when given different sensor records with same sensorId" in {
    val tested = new SensorStream().createSensorRecordFlow
    val readings = List(SensorRecord("s1", "1"), SensorRecord("s2", "5"), SensorRecord("s1", "12"))
    val res1 = SensorResult(scala.collection.mutable.HashMap("s1" -> SensorRecordDetail(1.0,12.0,13.0,2,0), "s2" -> SensorRecordDetail(5.0,5.0,5.0,1,0)))
    val res2 = SensorResult(scala.collection.mutable.HashMap("s1" -> SensorRecordDetail(1.0,12.0,13.0,2,0), "s2" -> SensorRecordDetail(5.0,5.0,5.0,1,0)))
    val res3 = SensorResult(scala.collection.mutable.HashMap("s1" -> SensorRecordDetail(1.0,12.0,13.0,2,0), "s2" -> SensorRecordDetail(5.0,5.0,5.0,1,0)))
    val flow = Source(readings).via(tested)

    flow.runWith(TestSink.probe[SensorResult])
      .request(3)
      .expectNextUnordered(res1, res2, res3)
      .expectComplete()
  }

  it should "return SensorResult when given different sensor records with same sensorId with NaN" in {
    val tested = new SensorStream().createSensorRecordFlow
    Thread.sleep(1000)
    val readings = List(SensorRecord("s1", "4"), SensorRecord("s2", "5"), SensorRecord("s1", "NaN"), SensorRecord("s2", "12"),
      SensorRecord("s3", "4"), SensorRecord("s1", "9"))
    val res1 = SensorResult(scala.collection.mutable.HashMap("s3" -> SensorRecordDetail(4, 4, 4, 1, 0), "s1" -> SensorRecordDetail(4, 9, 13, 2, 1),
      "s2" -> SensorRecordDetail(5, 12, 17, 2, 0)))
    val flow = Source(readings).via(tested)
    flow.runWith(TestSink.probe[SensorResult])
      .request(6)
      .expectNextUnordered(res1, res1, res1, res1, res1, res1)
      .expectComplete()
  }
}
