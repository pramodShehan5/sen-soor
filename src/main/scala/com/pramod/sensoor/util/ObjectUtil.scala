package com.pramod.sensoor.util

import com.pramod.sensoor.model.SensorRecord

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/**
 * This is using to object utilization
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

object ObjectUtil {
  implicit class TryToFuture(index: Try[SensorRecord]) {
    def convertTryToFuture: Future[SensorRecord] = {
      index match {
        case Success(value) => Future.successful(value)
        case Failure(exception) => Future.failed(exception)
      }
    }
  }
}