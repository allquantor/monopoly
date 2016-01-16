package com.haw.monopoly.web

import com.haw.monopoly.core.entities.dice.Dice
import com.haw.monopoly.core.services.{BoardService, GameService}
import com.haw.monopoly.data.repositories.{BoardRepository, GameRepository}
import org.json4s.JsonDSL._
import org.json4s.native.Serialization._
import org.json4s.{Formats, _}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport


/**
  * Created by Ivan Morozov on 24/10/15.
  */
class BoardsController(boardRepository: BoardRepository, gameRepository: GameRepository) extends ScalatraServlet with JacksonJsonSupport {
  override protected implicit def jsonFormats: Formats = DefaultFormats


  get("/:boardid/players") {

    val boardID = params("boardid")
    val playerUri = gameRepository.getById(boardID).map(_.players.map(_.uri)).get
    compact(render(("players" -> playerUri)))
  }

  get("/:boardid/players/:name") {
    val boardID = params("boardid")
    val playerName = params("name")

    val ourPlayer = boardRepository.getById(boardID).map(_.player.filter(_.id == playerName)).get.head


    parse(write(ourPlayer)) merge parse(
      s"""{ "place" : "/games/$boardID/places/${ourPlayer.position}",
          "roll" : "/boards/$boardID/players/${ourPlayer.id}/roll",
          "move" : "/boards/$boardID/players/${ourPlayer.id}/move"}""")

  }


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
    GameService.setPlayerReady(gameId, playerId, gameRepository, boardRepository)
      .getOrElse(status_=(404))
  }


  post("/:gameid/players/:playerid/roll") {

    val gameId = params("gameid")
    val playerId = params("playerid")

    val reqBodyJson = parse(request.body)

    val dice1 = (reqBodyJson \ "roll1").extract[Dice]
    val dice2 = (reqBodyJson \ "roll2").extract[Dice]

    val changedBoard = BoardService.getCurrentBoard(gameId, boardRepository).flatMap { board =>
      BoardService.changeBoardState(board, dice1, dice2, boardRepository, playerId)
    }.getOrElse(status_=(404))

    changedBoard

  }

  get("/:boardid/places") {

  }


  before() {
    contentType = formats("json")
  }

}
