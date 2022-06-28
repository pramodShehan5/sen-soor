package com.pramod.sensoor.controller

import com.pramod.sensoor.model.SensorRecord

import java.io.File
import scala.util.Try

/**
 * This is using to handle CSV files function such as get file list from the directory.
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

object CSVController {
  /***
   * here we can use factory to support all the file types
   * @param filePath
   * @return
   */
  def getFilesList(filePath: String): List[File] = {
    val directory = new File(filePath)
    if (directory.exists && directory.isDirectory) {
      directory.listFiles.filter(_.isFile).toList
    } else {
      println(s"No files in $filePath")
      List[File]()
    }
  }

  def parseLine(filePath: String)(line: String): Try[SensorRecord] = Try {
    val fields = line.split(",")
    SensorRecord(fields(0), fields(1).trim)
  }
}