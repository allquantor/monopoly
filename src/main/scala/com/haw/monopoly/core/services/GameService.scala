package com.haw.monopoly.core.services

import com.haw.monopoly.core.entities.board.Board
import com.haw.monopoly.core.player.{Place, PlayerBoards, PlayerGames}
import com.haw.monopoly.data.repositories.MutexStatusCodes.MutexStatus
import com.haw.monopoly.data.repositories.{BoardRepository, GameRepository}

/**
  * Created by Ivan Morozov on 23/11/15.
  */
object GameService {


  def isPlayerReady(gameId: String, playerId: String, gameRepo: GameRepository):Option[Boolean] = {
    gameRepo.getById(gameId).flatMap { game =>
      game.players.find(_.id == playerId).map {_.ready}
    }
  }


  def setPlayerReady(gameId: String, playerId: String, gameRepo: GameRepository, boardRepo: BoardRepository): Option[Boolean] = {

    def _updateBoard(player:PlayerGames) = {

      val playerBoards = PlayerBoards(player.id, Place("0","Start"), 0)

      BoardService.getCurrentBoard(gameId,boardRepo).map { board =>
        // our board can register a player only when the player already exists in games
        board.player.find(_.id == player.id).getOrElse {
          BoardService.addPlayerToBoard(gameId,playerBoards,boardRepo)
        }
      }.getOrElse {
        BoardService.createBoard(gameId,Set(playerBoards),boardRepo)
      }

    }

    Option(gameRepo.getById(gameId).flatMap { game =>
      game.players.find(_.id == playerId).flatMap { p =>

        _updateBoard(p)

        val updatedPlayer = p.copy(ready = true)
        val newPlayerSet = (game.players - p) + updatedPlayer
        val newUpdatedGame = game.copy(players = newPlayerSet)

        gameRepo.delete(gameId)
        gameRepo.create(newUpdatedGame)
      }
    }.isDefined)


  }



  def releaseMutexFor(gameId: String, playerId: String, gameRepository: GameRepository): Option[Boolean] = {
    gameRepository.releaseMutexFor(gameId, playerId)
  }


  def getPlayerPosition(gameId: String, playerId: String, gameRepository: GameRepository, boardRepository: BoardRepository): Option[Place] = {
      boardRepository.getById(gameId).flatMap { board =>
        board.player.find(_.id == playerId).map {
          _.place
        }
      }
  }



  def getBoardStatus(gameId: String, gameRepository: GameRepository, boardRepository: BoardRepository): Option[Board] = {
    gameRepository.getById(gameId).flatMap { game =>
      boardRepository.getById(gameId)
      }
  }


  def checkMutexFor(gameId: String, gameRepository: GameRepository, boardRepository: BoardRepository): Option[PlayerBoards] = {
    gameRepository.checkMutexForGame(gameId).flatMap { playerIdHolMutex =>
      boardRepository.getPlayerToBoard(gameId, playerIdHolMutex)
    }
  }


  def deleteMutexFor(gameId: String, gameRepository: GameRepository): Option[Boolean] = {
    gameRepository.deleteMutexForGame(gameId)
  }


  def checkMutexForPlayer(gameId:String,playerId:String, gameRepository: GameRepository):Option[MutexStatus] = {
    gameRepository.checkMutexForPlayer(gameId,playerId)
  }


  def registerPlayerInGame(gameId: String, playerId: String,
                           playerName: String, playerUri: String,
                           gameRepository: GameRepository): Option[PlayerGames] = {

    val newPlayer = PlayerGames(playerId, playerName, playerUri, false)

    gameRepository.getById(gameId).map { game =>

      game.players.find(_.id == playerId).getOrElse {
        val newPlayerSet = game.players + newPlayer
        val newGameObject = game.copy(gameid = gameId, players = newPlayerSet)

        gameRepository.delete(gameId).map { deleteResult =>
          gameRepository.create(newGameObject)
        }
        newPlayer
      }
    }
  }


}



