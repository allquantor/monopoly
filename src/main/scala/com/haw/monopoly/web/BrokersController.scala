package com.haw.monopoly.web

import com.haw.monopoly.core.entities.game.Game
import com.haw.monopoly.core.player.{Place, PlayerEvents}
import com.haw.monopoly.core.{Estate, Event}
import com.haw.monopoly.data.repositories.{BrokersRepository, EstateRepository}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

/**
  * Created by Ivan Morozov on 12/12/15.
  */
class BrokersController(brokersRepository: BrokersRepository, estateRepository: EstateRepository) extends ScalatraServlet with JacksonJsonSupport {
  override protected implicit def jsonFormats: Formats = DefaultFormats

  put("/:gameid") {
    val game = ((parse(request.body))).extract[Game]
    brokersRepository.create(game.gameid).getOrElse(status_=(404))
    game
  }

  put("/:gameid/places/:placeid") {

    val placeid = params("placeid")
    val gameId = params("gameid")

    val place = ((parse(request.body) \ "place")).extract[String]
    val owner = ((parse(request.body) \ "owner")).extract[String]
    val value = ((parse(request.body) \ "value")).extract[Int]
    val rent = ((parse(request.body) \ "rent")).extract[Array[Int]]
    val cost = ((parse(request.body) \ "cost")).extract[Array[Int]]
    val houses = ((parse(request.body) \ "houses")).extract[Int]

    val estate = Estate(placeid, place, owner, value, rent, cost, houses)

    estateRepository.create(estate, gameId).getOrElse(status_=(404))
  }

  post("/:gameid/places/:placeid/visit/:playerid") {
    val gameid = params("gameid")
    val placeid = params("placeid")
    val playerid = params("playerid")

    estateRepository.findByPlaceId(gameid, placeid).map {
      case someestate if (someestate.owner == playerid) => {
        Event("1", "Already Owned", "Aleady Owned Event Name", "Because You have it", "Ressource", PlayerEvents(
          playerid, "playerThatAlreadHave", "callMeMaybe.de", Place("id", placeid), 13, true))
      }
      case someestate if (someestate.owner == "null") => {
        Event("3", "You can buy it", "Free to buy", "On Market", "Ressource",null)
      }
      case someestate if (someestate.owner != playerid) => {
        Event("3", "Pay Rent", "RentToPay", "500", "Bank",PlayerEvents(
          "5", "Playaaa", "callMeMaybe.de", Place("id", "15"), 15, false))
      }
    }.getOrElse("THIS ESTATE DOES NOT EXIST IN OUR ESTATE REPOSITORY")


  }


  post("/:gameid/places/:placeid/owner") {
    val id = ((parse(request.body)) \ "id").extract[String]
    val gameid = params("gameid")
    val playerid = params("playerid")



    estateRepository.updateEstate(id, gameid, playerid)
  }



  before() {
    contentType = formats("json")
  }


}
