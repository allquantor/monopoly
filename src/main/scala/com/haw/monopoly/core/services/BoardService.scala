package com.haw.monopoly.core.services

import com.haw.monopoly.core.entities.board.Board
import com.haw.monopoly.core.entities.dice.Dice
import com.haw.monopoly.core.player.Player
import com.haw.monopoly.data.repositories.BoardRepository

/**
  * Created by Ivan Morozov on 07/11/15.
  */
object BoardService {


  def changeBoardState(board: Board, dice1: Dice, dice2: Dice, repo: BoardRepository): Board = {

    val b = repo.create(board)
    val _b = repo.getById(board.id)
    require(b == _b)

    // todo the logic
    board
  }


  def getCurrentBoard(gameId: String, repo: BoardRepository): Option[Board] = {
    repo.getById(gameId)
  }

  def createBoard(gameId: String, player: Set[Player], repo: BoardRepository): Option[Board] = {
    val neuerBoard = Board(gameId, player)
    repo.create(neuerBoard)
  }

}
