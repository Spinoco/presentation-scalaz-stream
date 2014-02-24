package com.spinoco.presentation

import scalaz.stream.{wye, Process}
import scalaz.concurrent.Task
import scala.util.Random
import scalaz.stream.Process.Process1

/**
 * @author jakub.ryska
 *         Date: 2/23/14
 *         Time: 7:29 PM
 */
object Presentation {

  /**
   * Simulates fetching different organization names, then creating hostnames from it,
   * then merging it into one stream
   *
   * @param args
   */
  def main(args:Array[String]):Unit = {

    val sites1 = List("google", "scala-lang", "spinoco", "twitter", "github")
    val sites2 = List("goal", "myspace", "stackoverflow", "atlassian")

    /**
     * Source of origanization names.
     *
     * @param sites source of sites (this is an example program, in real world it would fetch the site from db for example)
     * @param mark mark the stream with string, so it can be distinguished from other streams
     * @param wait value that simulates doing some work, so we can watch, how this method is emitting elements
     *
     * @return source of (mark, hostname)
     */
    def urlSource(sites:List[String], mark:String = "1", wait: => Unit = ()):Process[Task, String Pair String] = Process.await {
      val site = sites((new Random()).nextInt(sites.length))
      Task{
        wait
        s"$site.com"
      }
    }(element => Process.emit(element -> mark) fby urlSource(sites, mark, wait))

    /**
     * Take an organization site, evaluate it's value
     *
     * @param string organization site
     * @return value if the site can be evaluated
     */
    def rankFunction(string:String):Process[Task, String] = {
      val res = string.toList match {
        case 's' :: _ => Process.emit("nice page")
        case 'g' :: _ => Process.emit("quite nice page")
        case _ => Process.halt
      }
      res.toSource
    }

    /**
     * Simply print everything in the console
     */
    val printProcess:Process.Sink[Task, Any] = Process.repeatEval {
      Task.now((x:Any) => Task.now(println(x)))
    }

    val url1 = urlSource(sites1, "1", Thread.sleep(1000L))
    val url2 = urlSource(sites2, "2", Thread.sleep(200L))
    val wyed = url1.wye(url2)(wye.merge)

    /**
     * Zip the elements with running sum of all elements, that have passed this process
     *
     * @param count stating count of elements
     * @tparam A
     * @return
     */
    def numElements[A](count:Int = 0):Process1[A, (A, Int)] = Process.receive1{
      element => Process.emit(element -> (count + 1)) fby numElements((count + 1))
    }

    val rankStream = for {
      ((url, mark), num) <- (wyed |> numElements(0))
      rank <- rankFunction(url)
    } yield ((mark, rank, url, num))

    (rankStream through printProcess).take(10).run.run

  }

}
