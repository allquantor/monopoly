package com.haw.monopoly.web

import com.haw.monopoly.core.Event
import com.haw.monopoly.core.player.Place
import com.haw.monopoly.data.repositories.PlayersPrivateRepository
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

/**
  * Created by Ivan Morozov on 10/12/15.
  */
class PlayersController(playerRepo:PlayersPrivateRepository) extends ScalatraServlet with JacksonJsonSupport {

  override protected implicit def jsonFormats: Formats = DefaultFormats

  val myOwnId = 999

  post("/turn") {
    playerRepo.setReady(myOwnId.toString,true).getOrElse(status_=(503))
  }

  post("/event") {
    val _type = ((parse(request.body)) \ "type").extract[String]
    val name = ((parse(request.body)) \ "name").extract[String]
    val uri = ((parse(request.body)) \ "uri").extract[String]
    val position = ((parse(request.body)) \ "position").extract[Int]
    val ready = ((parse(request.body)) \ "ready").extract[Boolean]
    val place = ((parse(request.body)) \ "place").extract[Place]
    val event = Event(_type,name,uri,place,position,ready)

    playerRepo.updateCurrentState(myOwnId.toString,event).getOrElse(status_=(503))
  }
  before() {
    contentType = formats("json")
  }
}
