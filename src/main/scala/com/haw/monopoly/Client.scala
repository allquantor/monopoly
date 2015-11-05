package com.haw.monopoly

import com.haw.monopoly.data.DataModule
import com.haw.monopoly.web.WebModule
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import scalaj.http._
import scalaj.http.HttpConstants._






object Client extends App with WebModule with DataModule {
  val logger = LoggerFactory.getLogger(getClass)
  val config = ConfigFactory.load()

  //val url =  config.getString("http.url")
  //val port = config.getInt("http.port")

  //val _url = s"$url:$port"

  val _url = "http://localhost:80"


   val request: HttpRequest = MyHttp(_url)




  println(Http("http://www.google.de").param("q", "monkeys").asString)



}
