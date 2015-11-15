package com.haw.monopoly.web

import com.haw.monopoly.core.entities.dice.Dice
import com.haw.monopoly.core.services.BoardService
import com.haw.monopoly.data.repositories.BoardRepository
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

/**
  * Created by Ivan Morozov on 24/10/15.
  */
class BoardsController(boardRepository: BoardRepository) extends ScalatraServlet with JacksonJsonSupport {
  override protected implicit def jsonFormats: Formats = DefaultFormats


  post("/:gameid/players/:playerid/roll") {

    val gameId = params("gameid")
    val playerId = params("playerid")

    val reqBodyJson = parse(request.body)

    val dice1 = (reqBodyJson \ "roll1").extract[Dice]
    val dice2 = (reqBodyJson \ "roll2").extract[Dice]


    val changedBoard = BoardService.getCurrentBoard(gameId, boardRepository).map { board =>
      BoardService.changeBoardState(board, dice1, dice2, boardRepository, playerId)
    }.getOrElse(throw new Exception("Something went wrong here..."))

    changedBoard

  }


  before() {
    contentType = formats("json")
  }

}
