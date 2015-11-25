package com.haw.monopoly.web

import com.haw.monopoly.core.services.GameService
import com.haw.monopoly.data.repositories.MutexStatusCodes._
import com.haw.monopoly.data.repositories.{BoardRepository, GameRepository}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport
import org.slf4j.LoggerFactory

/**
  * Created by Ivan Morozov on 06/11/15.
  */
class GamesController(gameRepo: GameRepository,
                      boardRepo: BoardRepository
                     ) extends ScalatraServlet with JacksonJsonSupport {

  val logger = LoggerFactory.getLogger(getClass)


  override protected implicit def jsonFormats: Formats = DefaultFormats


  post("/") {
    logger.info(s"Trying to create game")
    val gameId = (scala.util.Random.nextInt(100000) + 1).toString
    val result = gameRepo.create(gameId)
    logger.info(s"Game creation result ${result}")
    result
  }



  put("/:gameid/players/:playerid") {

    val gameId = params("gameid")
    val playerId = params("playerid")
    val name = params("name")
    val uri = params("uri")

    GameService.registerPlayerInGame(
      gameId, playerId, name, uri, gameRepo
    ).getOrElse {
      status_=(404)
    }
  }






  get("/:gameid/players/:playerid/ready") {

    val gameId = params("gameid")
    val playerId = params("playerid")

    GameService.isPlayerReady(gameId, playerId, gameRepo).getOrElse {
      status_=(404)
    }
  }



  get("/:gameid/players/:playerid") {

    val gameId = params("gameid")
    val playerId = params("playerid")

    GameService.getPlayerPosition(gameId, playerId, gameRepo, boardRepo).getOrElse(
      status_=(404)
    )

  }

  get("/:gameid") {
    val gameId = params("gameid")
    gameRepo.getById(gameId).getOrElse {
      status_=(404)
      "GAME NOT FOUND"
    }
  }

  put("/:gameid/turn") {

    val gameId = params("gameid")

    val playerId = ((parse(request.body)) \ "playerid").extract[String]

    val statusCode = gameRepo.setMutexForGame(gameId, playerId).map(statusToCode)

    status_=(statusCode.getOrElse(503))

  }


  get("/:gameid/turn") {

    val gameId = params("gameid")
    GameService.checkMutexFor(gameId, gameRepo, boardRepo).getOrElse {
      status_=(404)
    }

  }

  delete("/:gameid/turn") {
    val gameId = params("gameid")
    GameService.deleteMutexFor(gameId, gameRepo).getOrElse {
      status_=(503)
    }
  }



  put("/:gameid/players/:playerid/ready") {

    val gameId = params("gameid")
    val playerId = params("playerid")

    GameService.setPlayerReady(gameId, playerId, gameRepo, boardRepo).getOrElse {
      status_=(404)
    }

  }


  before() {
    contentType = formats("json")
  }

}
