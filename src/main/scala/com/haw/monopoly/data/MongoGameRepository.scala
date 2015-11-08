package com.haw.monopoly.data

import com.haw.monopoly.core.Location
import com.haw.monopoly.data.repositories.{MutexStatusCodes, GameRepository}
import MutexStatusCodes.MutexStatus
import com.haw.monopoly.data.repositories.GameRepository
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.commons.ValidBSONType.BSONObject
import org.bson.types.ObjectId

/**
  * Created by Ivan Morozov on 06/11/15.
  */
class MongoGameRepository(collectionGames: MongoCollection) extends GameRepository {
  override def all(limit: Option[Int]): Seq[Location] = ???

  override def byId(id: ObjectId): Option[Location] = ???

  override def byTextPhrase(phrase: String, limit: Option[Int]): Seq[Location] = ???

  override def byNameFragment(name: String, limit: Option[Int]): Seq[Location] = ???


  override def putMutexForGame(id: String, playerId: String): Option[MutexStatus] = {

    val gameObj = MongoDBObject("gameId" -> id)

    collectionGames.find(gameObj).map { found =>
      collectionGames.find()
      found
    }
    //todo implement
  null
  }
}
