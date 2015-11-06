package com.haw.monopoly.web

import com.haw.monopoly.core.game.GameRepository
import com.haw.monopoly.core.game.MutexStatusCodes._
import org.json4s.Formats
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

/**
  * Created by Ivan Morozov on 06/11/15.
  */
class GamesController(gameRepo: GameRepository) extends ScalatraServlet with JacksonJsonSupport {
  override protected implicit def jsonFormats: Formats = ???

  put(":gameid/players/:playerid") {

  }

  get("/:gameid") {

  }

  put(":gameid/turn") {

    val gameId = params("gameId")

    val playerId = (parse(request.body) \ "playerid").asInstanceOf[String]

    val statusCode = gameRepo.putMutexForGame(gameId, playerId).map(statusToCode)

    status_=(statusCode.getOrElse(503))

  }


  get(":gameid/turn") {

  }

  delete(":gameid/turn") {

  }

  put(":gameid/players/:playerid/ready") {

  }


}
