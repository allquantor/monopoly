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
    val game = ((parse(request.body))).extract[Game]
    brokersRepository.create(game.gameid).getOrElse(status_=(404))
    game
  }

  put("/:gameid/places/:placeid") {

    val placeid = params("placeid")
    val gameId = params("gameid")

    val place = ((parse(request.body) \ "place" )).extract[String]
    val owner = ((parse(request.body) \ "owner" )).extract[String]
    val value = ((parse(request.body) \ "value" )).extract[Int]
    val rent = ((parse(request.body) \ "rent" )).extract[Array[Int]]
    val cost = ((parse(request.body) \ "cost" )).extract[Array[Int]]
    val houses = ((parse(request.body) \ "houses" )).extract[Int]

    val estate = Estate(placeid,place,owner,value,rent,cost,houses)

    estateRepository.create(estate,gameId).getOrElse(status_=(404))
  }

  post("/:gameid/places/:placeid/visit/:playerid") {
    val gameid = params("gameid")
    val placeid = params("placeid")
    val playerid = params("playerid")

    estateRepository.findByPlaceId(gameid,placeid).map { estate =>
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



  before() {
    contentType = formats("json")
  }


}
