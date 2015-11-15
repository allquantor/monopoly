package com.haw.monopoly.core.services

import com.haw.monopoly.core.entities.board.Board
import com.haw.monopoly.core.entities.dice.Dice
import com.haw.monopoly.core.player.Player
import com.haw.monopoly.data.repositories.BoardRepository

/**
  * Created by Ivan Morozov on 07/11/15.
  */
object BoardService {


  def changeBoardState(board: Board, dice1: Dice, dice2: Dice, repo: BoardRepository, playerId: String): Board = {

    val changedPlayer = board.player.find(_.id == playerId).map { player =>
      player.copy(id = playerId,
        place = player.place.copy(player.place.id,
          "JUST SUCCESSFULLY MOVED STREET")
      )
    }.getOrElse(throw new Exception("No player was found"))

    board.copy(player = (board.player.filter(_.id != playerId) + changedPlayer))
  }


  def getCurrentBoard(gameId: String, repo: BoardRepository): Option[Board] = {
    repo.getById(gameId)
  }

  def createBoard(gameId: String, player: Set[Player], repo: BoardRepository): Option[Board] = {
    repo.create(Board(gameId, player))
  }

}
