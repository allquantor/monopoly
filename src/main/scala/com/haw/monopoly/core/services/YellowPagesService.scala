package com.haw.monopoly.core.services

import java.net.InetAddress

import dispatch.{Http, as, url}
import org.json4s._
import org.json4s.native.Serialization.write
import org.slf4j.LoggerFactory
import scala.concurrent.{Await, ExecutionContext}
import ExecutionContext.Implicits.global
import scala.concurrent.duration._



/**
  * Created by Ivan Morozov on 25/11/15.
  */
object YellowPagesService {

  val logger = LoggerFactory.getLogger(getClass)

  implicit val formats = DefaultFormats

  val headers = Map("Content-type" -> "application/json")

  case class OurService(name: String, description: String, service: String, uri: String)

  val yellowPagesUrl = url("https://vs-docker.informatik.haw-hamburg.de/ports/8053/services")

  val ourService = OurService("board", "board-service", "binboard", _getCurrentIp.toString)


  def _getCurrentIp = InetAddress.getLocalHost()


  def registerOurService = {

    logger.info(s"Starting to register our service on url:${yellowPagesUrl.url} AND send our service ${ourService}")

    val request = yellowPagesUrl << (write(ourService))  <:< headers

    logger.info(s"Starting to register our service with request:${request}")

    val future = Http(request.POST OK as.String).map { response =>
      logger.info(s"Our Request Yield The Response: ${response}")
    }

    Await.result(future, 5 seconds)
  }


}
