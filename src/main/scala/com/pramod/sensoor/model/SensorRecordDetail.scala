package com.pramod.sensoor.model

/**
 * This is using to keep min, max, total, count and invalid count of unique sensor
 * Count -> all the processing records(valid + invalid)
 * Total ->  total of the valid records value
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

case class SensorRecordDetail(min: Double = 0.0,
                              max: Double = 0.0 ,
                              total: Double = 0.0,
                              count: Int = 0,
                              invalidCnt: Int = 0)