package com.pramod.sensoor.exception

/**
 * This is custom exception
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

case class SensorException(errorMessage: String,
                           cause: Throwable = null) extends Exception(errorMessage, cause)