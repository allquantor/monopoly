package com.haw.monopoly.data

import com.haw.monopoly.core.entities.board.Board
import com.haw.monopoly.core.player.PlayerBoards
import com.haw.monopoly.data.collections.BoardCollection
import com.haw.monopoly.data.repositories.BoardRepository
import com.mongodb.casbah.commons.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import com.novus.salat._

import scala.util.Try

/**
  * Created by Ivan Morozov on 07/11/15.
  */
class MongoBoardRepository(boardCollection: BoardCollection) extends BoardRepository with SalatContext {

  override def delete(board: Board): Boolean = {
    boardCollection.collection.findAndRemove(MongoDBObject("id" -> board.id)).isDefined
  }

  override def getById(id: String): Option[Board] = {

    boardCollection.collection.findOne(MongoDBObject("id" -> id)) match {
      case Some(document) =>
        Some(grater[Board].asObject(document))
      case None => None
    }
  }

  override def create(board: Board): Option[Board] = {

    val newboard = grater[Board].asDBObject(board)

    Try(boardCollection.collection.save(newboard)).map { writeResult =>
      Option(board)
    }.getOrElse(None)


  }

  override def update(board: Board): Option[Board] = {
    val newboard = grater[Board].asDBObject(board)
    delete(board)
    boardCollection.collection.save(newboard)
    Some(board)
  }

  override def getPlayerToBoard(id: String, playerId: String): Option[PlayerBoards] = {
    getById(id).flatMap { board =>
      board.player.find(_.id == playerId)
    }
  }
}
