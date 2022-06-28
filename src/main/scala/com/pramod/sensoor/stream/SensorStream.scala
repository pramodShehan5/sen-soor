package com.pramod.sensoor.stream

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Balance, Flow, Framing, GraphDSL, Merge, Sink, Source, StreamConverters}
import akka.stream.{ActorAttributes, FlowShape, Supervision}
import akka.util.{ByteString, Timeout}
import com.pramod.sensoor.actor.SensorActor
import com.pramod.sensoor.config.SensorConf
import com.pramod.sensoor.controller.{CSVController, RecordController}
import com.pramod.sensoor.model.{SensorRecord, SensorResult}
import com.pramod.sensoor.util.ObjectUtil._

import java.io.{File, FileInputStream}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

/**
 * This is the main stream to handle it.
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

class SensorStream(implicit system: ActorSystem) extends SensorConf {

  val sensorActor = system.actorOf(SensorActor.props())

  //csv file sensor records line delimiter
  val lineDelimiter: Flow[ByteString, ByteString, NotUsed] =
    Framing.delimiter(ByteString("\n"), 128, allowTruncation = true)

  /** *
   * This is the flow via calling after lineDelimiter and convert it to String
   * and create case class using sensorId and sensorValue.
   * line converting to Case class is doing in paralally
   */
  val parseFile: Flow[File, SensorRecord, NotUsed] =
    Flow[File].flatMapConcat { file =>
      val gZipInputStream = new FileInputStream(file)
      StreamConverters.fromInputStream(() => gZipInputStream)
        .via(lineDelimiter)
        .drop(noOflinkSkip)
        .map(_.utf8String)
        .mapAsync(parallelismProcessing)(oneLine => CSVController.parseLine(file.getPath)(oneLine).convertTryToFuture)
    }

  /***
   * here we are using actor. actor is using to handle the all the sensor records
   * We are using case class with all the details of unique sensors such as max, min, total, count & failures
   * return SensorResult with Map
   */
  val createSensorRecordFlow: Flow[SensorRecord, SensorResult, NotUsed] = Flow[SensorRecord]
    .mapAsync(parallelismProcessing) { m =>
      import akka.pattern.ask
      implicit val askTimeout = Timeout(30.seconds)
      (sensorActor ? m)
        .mapTo[SensorResult]
        .map(result => result)
    }

  def init(directoryPath: String)(implicit system: ActorSystem): Unit = {
    val sinkOutPut = Sink.foldAsync[SensorResult, SensorResult](SensorResult())((result, n) => result.getFutureResult(n))

    val graph = GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._

      val balance = builder.add(Balance[File](partitionCount))
      val merge = builder.add(Merge[SensorResult](partitionCount))

      (1 to partitionCount).foreach { _ =>
        balance ~> parseFile ~> createSensorRecordFlow ~> merge
      }
      FlowShape(balance.in, merge.out)
    }

    val files = CSVController.getFilesList(directoryPath)
    Source(files)
      .via(graph)
      .withAttributes(ActorAttributes.supervisionStrategy { e =>
        println("Exception thrown during sensor processing", e)
        Supervision.Resume
      })
      .runWith(sinkOutPut)
      .andThen {
        case Success(ss) =>
          println(s"Num of processed files ${files.length}")
          RecordController.printFinalResult(ss)
          println(s"Sensor streaming finished!")
        case Failure(e) =>
          println("Sensor processing exception here => ", e)
      }
  }
}