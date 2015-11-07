package com.haw.monopoly.gamelogic

import com.haw.monopoly.GameConfigs
import com.haw.monopoly.core.game.Game
import dispatch._
import org.json4s._

import scala.concurrent.ExecutionContext.Implicits.global


/**
  * Created by Ivan Morozov on 05/11/15.
  */
object GameLogic extends GameConfigs {
  implicit val formats = org.json4s.DefaultFormats

  type GameId = String


  def registerGame(implicit req: Req): Future[Game] = {

    Http(req.POST./("game") OK as.json4s.Json).map { json =>
      json.extract[Game]
    }
  }

  def registerPlayer(game: Game)(implicit req: Req, playerUri: OurPlayerLink): Future[String] = {
    Http(
      req.PUT
        ./("game")./(game.number)
        ./("player")./(ourId)
        .addQueryParameter("Playername", "BESTENAME")
        .addQueryParameter("Playeruri", playerUri) OK as.String
    )
  }



}
