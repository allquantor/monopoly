package com.haw.monopoly.data.repositories

import com.haw.monopoly.core.entities.board.Board
import com.haw.monopoly.core.player.PlayerBoards

/**
  * Created by Ivan Morozov on 07/11/15.
  */
trait BoardRepository {

  def getById(id: String): Option[Board]

  def create(board: Board): Option[Board]

  def update(board: Board): Option[Board]

  def delete(board: Board): Boolean

  def getPlayerToBoard(id:String,playerId:String):Option[PlayerBoards]

}
