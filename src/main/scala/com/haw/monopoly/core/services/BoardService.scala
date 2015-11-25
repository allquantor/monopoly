package com.haw.monopoly.core.services

import com.haw.monopoly.core.entities.board.Board
import com.haw.monopoly.core.entities.dice.Dice
import com.haw.monopoly.core.player.PlayerBoards
import com.haw.monopoly.data.repositories.BoardRepository

/**
  * Created by Ivan Morozov on 07/11/15.
  */
object BoardService {


  def changeBoardState(board: Board, dice1: Dice, dice2: Dice, repo: BoardRepository, playerId: String): Option[Board] = {

    val changedPlayer = board.player.find(_.id == playerId).map { p =>
      p.copy(id = playerId,
        place = p.place.copy(p.place.id,
          "JUST SUCCESSFULLY MOVED - STREET")
      ,position = p.position + dice1.number + dice2.number)
    }.getOrElse(throw new Exception("No player was found"))

    val newBoard = board.copy(player = (board.player.filter(_.id != playerId) + changedPlayer))
    repo.update(newBoard)
  }


  def getCurrentBoard(gameId: String, repo: BoardRepository): Option[Board] = {
    repo.getById(gameId)
  }

  def createBoard(gameId: String, player: Set[PlayerBoards], repo: BoardRepository): Option[Board] = {
    repo.create(Board(gameId, player))
  }

  def addPlayerToBoard(gameId:String,p:PlayerBoards,repo:BoardRepository): Option[Board] = {
    repo.getById(gameId).flatMap { board =>
      val newBoard = board.copy(player = board.player + p)
      repo.update(newBoard)
    }
  }

}
