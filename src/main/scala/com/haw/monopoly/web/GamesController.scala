package com.haw.monopoly.web

import com.haw.monopoly.core.entities.game.{GameWithPlayerLinks, Game, Components}
import com.haw.monopoly.core.services.GameService
import com.haw.monopoly.data.repositories.MutexStatusCodes._
import com.haw.monopoly.data.repositories.{BoardRepository, GameRepository}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport
import org.slf4j.LoggerFactory
import dispatch._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * Created by Ivan Morozov on 06/11/15.
  */
class GamesController(gameRepo: GameRepository,
                      boardRepo: BoardRepository
                     ) extends ScalatraServlet with JacksonJsonSupport {

  val logger = LoggerFactory.getLogger(getClass)

  val ourUri = "localhost:9999"


  override protected implicit def jsonFormats: Formats = DefaultFormats


  get("/") {
    logger.info("Try to fetch all games")
    gameRepo.getAllGames.map { g =>
      GameWithPlayerLinks(
        g.gameid,
        g.uri,
        g.players.map(_.uri),
        g.components
      )
    }
  }


  post("/") {
    logger.info(s"Trying to create game")
    val gameId = (scala.util.Random.nextInt(100000) + 1).toString

    val components = ((parse(request.body)) \ "components").extract[Components]

    val game = Game(gameId,ourUri,Set(),components)
    val result = gameRepo.create(game)

    logger.info(s"Game creation result ${result}")

    // todo adresse to boards from components object getten
    val myreq = host("localhost",9999)

     Http(myreq / "boards" / s"$gameId" PUT).onComplete {
      case Success(r) => {
        logger.info(s"We had an successful board creation to gameId:${gameId} and got Board ${r.toString}")
        val boardId =  ((parse(r.getResponseBody)) \ "id").extract[String]

        val changedComponents = components.copy(board = components.board + s"/$boardId")
        gameRepo.delete(gameId)
        gameRepo.create(game.copy(components=changedComponents))

      }
      case Failure(r) =>  {
        logger.info(s"We got an failure by board creation from game ${r.getMessage}")
        "NO-BOARD-CREATED"
      }
    }

    response.setHeader("location",result.get.uri)

    gameRepo.getById(game.gameid).getOrElse(status_=(503))

  }




  get("/:gameid/players/:playerid/ready") {

    val gameId = params("gameid")
    val playerId = params("playerid")

    GameService.isPlayerReady(gameId, playerId, gameRepo).getOrElse {
      status_=(404)
    }
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
    val req = host("localhost", 9999)

    Http(req / "boards" / s"$gameId" / "players" / s"$playerId" PUT).onComplete {
      case Success(r) => logger.info(s"We had an successful Player set on board for playerid:${playerId} and got ${r.getResponseBody}")
      case Failure(r) => logger.info(s"We got an failure by player settion ${r.getMessage}")
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
