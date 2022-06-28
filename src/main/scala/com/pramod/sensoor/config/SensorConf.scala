package com.pramod.sensoor.config

import com.typesafe.config.ConfigFactory

/**
 * This is the configuration file to handle all the configurations
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

trait SensorConf {
  val sensorConf = ConfigFactory.load("sensor.conf")
  lazy val noOflinkSkip = sensorConf.getInt("no-line-skip")
  lazy val parallelismProcessing = sensorConf.getInt("parallelism-processing")
  lazy val partitionCount = sensorConf.getInt("partition-count")
}