import akka.actor.ActorSystem
import com.pramod.sensoor.stream.SensorStream

import scala.io.StdIn.readLine

/**
 * This is the main class
 *
 * @author pramod shehan(pramodshehan@gmail.com)
 */

object Main {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem.create("sen-soor")
    println("Welcome to the ses-soor!!! ")
    print("Enter sensor record directory path =>\n")

    val path = readLine()
    new SensorStream().init(path)
  }
}