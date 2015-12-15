package com.haw.monopoly.web

import com.haw.monopoly.core.entities.dice.Dice
import com.haw.monopoly.core.services.{BoardService, GameService}
import com.haw.monopoly.data.repositories.{BoardRepository, GameRepository, MutexStatusCodes}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

/**
  * Created by Ivan Morozov on 24/10/15.
  */
class BoardsController(boardRepository: BoardRepository, gameRepository: GameRepository) extends ScalatraServlet with JacksonJsonSupport {
  override protected implicit def jsonFormats: Formats = DefaultFormats


  put("/:gameid") {

    val gameId = params("gameid")

    BoardService.
      createBoard(gameId, Set(), boardRepository).
      getOrElse(status_=(404))
  }

  put("/:gameid/players/:playerid") {
    val gameId = params("gameid")
    val playerId = params("playerid")


    // our board can register a player only when the player already exists in games
    GameService.setPlayerReady(gameId,playerId,gameRepository,boardRepository)
      .getOrElse(status_=(404))
  }


  post("/:gameid/players/:playerid/roll") {

    val gameId = params("gameid")
    val playerId = params("playerid")

    val reqBodyJson = parse(request.body)

    val dice1 = (reqBodyJson \ "roll1").extract[Dice]
    val dice2 = (reqBodyJson \ "roll2").extract[Dice]


    val changedBoard = GameService.checkMutexForPlayer(gameId, playerId, gameRepository) match {
      case Some(MutexStatusCodes.AlreadyHolding) =>

        BoardService.getCurrentBoard(gameId, boardRepository).flatMap { board =>
          BoardService.changeBoardState(board, dice1, dice2, boardRepository, playerId)
        }.getOrElse(status_=(404))

      case _ => (status_=(404))
    }



    changedBoard

  }


  before() {
    contentType = formats("json")
  }

}
