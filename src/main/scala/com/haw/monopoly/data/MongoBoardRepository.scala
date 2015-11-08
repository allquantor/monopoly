package com.haw.monopoly.data

import com.haw.monopoly.core.entities.board.Board
import com.haw.monopoly.data.repositories.BoardRepository
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.commons.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import com.novus.salat._

import scala.util.Try

/**
  * Created by Ivan Morozov on 07/11/15.
  */
class MongoBoardRepository(collection: MongoCollection) extends BoardRepository with SalatContext {

  override def delete(board: Board): Boolean = {
    collection.findAndRemove(MongoDBObject("id" -> board.id, "players" -> board.player)).isDefined
  }


  override def getById(id: String): Option[Board] = {

    collection.findOne(MongoDBObject("id" -> id)) match {
      case Some(document) => Some(grater[Board].asObject(document))
      case None => None
    }
  }

  override def create(board: Board): Option[Board] = {

    val newboard = MongoDBObject("id" -> board.id, "players" -> board.player)
    Try(collection.save(newboard)).map { writeResult =>
      Option(board)
    }.getOrElse(None)


  }

  override def update(board: Board): Option[Board] = {
    val newboard = MongoDBObject("id" -> board.id, "players" -> board.player)
    Try(collection.save(newboard)).map { writeResult =>
      writeResult.isUpdateOfExisting match {
        case true => Option(board)
        case _ => None
      }
    }.getOrElse(None)

  }
}
