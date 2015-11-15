package com.haw.monopoly.data

import com.haw.monopoly.core.Location
import com.haw.monopoly.data.repositories.{GameRepository, MutexStatusCodes}
import com.haw.monopoly.data.repositories.MutexStatusCodes.MutexStatus
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.commons.MongoDBObject
import org.bson.types.ObjectId

/**
  * Created by Ivan Morozov on 06/11/15.
  */
class MongoGameRepository(collectionGames: MongoCollection) extends GameRepository {
  override def all(limit: Option[Int]): Seq[Location] = ???

  override def byId(id: ObjectId): Option[Location] = ???

  override def byTextPhrase(phrase: String, limit: Option[Int]): Seq[Location] = ???

  override def byNameFragment(name: String, limit: Option[Int]): Seq[Location] = ???

  /**
    * Specification for the Game-Mutex-Object
    * Mutex => MongoDBObject(gameId -> id, playerId -> id)
    *
    * @param id
    * @param playerId
    * @return
    */

  override def setMutexForGame(id: String, playerId: String): MutexStatus = {

    val gameObj = MongoDBObject("gameId" -> id, "playerId" -> "*".r)

    collectionGames.find(gameObj).map { found =>
      found.get("playerId").asInstanceOf[String] == playerId match {
        case true => MutexStatusCodes.AlreadyHolding
        case false => MutexStatusCodes.AquiredByAnother
      }
    }.toList.headOption.getOrElse {
      collectionGames.save(MongoDBObject("gameId" -> id, "playerId" -> playerId))
      MutexStatusCodes.AquiredSuccess
    }

  }
}
