package com.haw.monopoly

import com.haw.monopoly.data.DataModule

import com.haw.monopoly.gamelogic.GameLogic._
import com.haw.monopoly.web.WebModule
import com.typesafe.config.Config
import dispatch.Defaults._
import dispatch._

import scala.concurrent.Await
import scala.concurrent.duration._


trait GameConfigs {


  val ourHost = "localhost"
  val ourPort = 8080
  val ourId = "player1"

  type OurPlayerLink = String

  implicit val defaultHost = host("192.168.99.100", 8080)

  implicit val defaultPlayer: OurPlayerLink = s"http://localhost:4567/player/$ourId"


}


object Client extends App with WebModule with DataModule with GameConfigs {


  override val config: Config = null


  val playerRegistration = registerGame flatMap registerPlayer







  // val logger = LoggerFactory.getLogger(getClass)
  //val config = ConfigFactory.load()

  //val myHost = host(ourHost, ourPort).POST


  // register game
  //val _gameId = Await.result(registerGame(myHost),1 seconds)


  // registerPlayer
  //registerPlayer("dummy","dummyHost","dummyGameId")


  //val url =  config.getString("http.url")
  //val port = config.getInt("http.port")

  //val _url = s"$url:$port"


}
