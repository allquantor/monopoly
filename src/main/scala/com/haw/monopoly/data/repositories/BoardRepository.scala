package com.haw.monopoly.data.repositories

import com.haw.monopoly.core.entities.board.Board

/**
  * Created by Ivan Morozov on 07/11/15.
  */
trait BoardRepository {

  def getById(id: String): Option[Board]

  def create(board: Board): Option[Board]

  def update(board: Board): Option[Board]

  def delete(board: Board): Boolean

}
