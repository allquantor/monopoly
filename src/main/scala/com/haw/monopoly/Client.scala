package com.haw.monopoly

import com.haw.monopoly.data.DataModule
import com.haw.monopoly.gamelogic.GameLogic._
import com.haw.monopoly.web.WebModule
import com.typesafe.config.ConfigFactory
import dispatch._
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.concurrent.duration._


trait GameConfigs {
  val ourHost = "http://google.de"
  val ourPort = 8080
  val ourId = "player1"



}


object Client extends App with WebModule with DataModule with GameConfigs {

  val logger = LoggerFactory.getLogger(getClass)
  val config = ConfigFactory.load()

  val myHost = host(ourHost, ourPort).POST


  // register game
   val _gameId = Await.result(registerGame(myHost),1 seconds)


  // registerPlayer
  registerPlayer("dummy","dummyHost","dummyGameId")








  //val url =  config.getString("http.url")
  //val port = config.getInt("http.port")

  //val _url = s"$url:$port"














}
