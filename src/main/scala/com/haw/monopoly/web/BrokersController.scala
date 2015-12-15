package com.haw.monopoly.web

import com.haw.monopoly.core.Estate
import com.haw.monopoly.core.entities.game.Game
import com.haw.monopoly.data.repositories.{EstateRepository, BrokersRepository}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

/**
  * Created by Ivan Morozov on 12/12/15.
  */
class BrokersController(brokersRepository: BrokersRepository,estateRepository: EstateRepository) extends  ScalatraServlet with JacksonJsonSupport{
  override protected implicit def jsonFormats: Formats = DefaultFormats

  put("/:gameid") {
    val game = ((parse(request.body)) \ "game").extract[Game]
    brokersRepository.create( game.gameid).getOrElse(status_=(404))
  }

  put("/:gameid/places/:placeid") {
    val estate = ((parse(request.body)) \ "estate").extract[Estate]
    val gameId = params("gameId")
    estateRepository.create(estate,gameId).getOrElse(status_=(404))
  }

  post("/:gameid/places/:placeid/visit/:playerid") {
    val gameid = params("gameid")
    val placeid = params("placeid")
    val playerid = params("playerid")

    estateRepository.findById(gameid,placeid).map { estate =>

      // was soll hier passieren
      // case 1  => Grundstück gehört bereits mir kann häuser drauf bauen
      // case 2 => Grundstück gehört jemanden anderen ich muss berechenen wieviel häuser er hat und miete an ihn zahlen
      // case 3 => Grundstück gehört  keinen soll ich es kaufen ja oder nein wocher weiss ich das? Wennn notifizieren ich wenn ich es weiss
      estate.owner
    }.getOrElse(status_=(404))
  }


  post("/:gameid/places/:placeid/owner") {
    val id = ((parse(request.body)) \ "id").extract[String]
    val gameid = params("gameid")
    val playerid = params("playerid")


    // 409 wenn es bereits jemand hat
    // sonnst 202 falls erfolgreich
    estateRepository.updateEstate(id,gameid,playerid)
  }











}
