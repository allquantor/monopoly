package com.haw.monopoly.data

import com.haw.monopoly.OurServer._
import com.haw.monopoly.core.entities.game.Game
import com.haw.monopoly.core.player.PlayerBoards
import com.haw.monopoly.core.services.BoardService
import com.haw.monopoly.data.collections.{MutexCollection, GamesCollection}
import com.haw.monopoly.data.repositories.MutexStatusCodes.MutexStatus
import com.haw.monopoly.data.repositories.{GameRepository, MutexStatusCodes}
import com.mongodb.casbah.commons.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import com.novus.salat._
import org.slf4j.LoggerFactory

import scala.util.Try

/**
  * Created by Ivan Morozov on 06/11/15.
  */
class MongoGameRepository(collectionGames: GamesCollection, collectionMutex: MutexCollection) extends GameRepository with SalatContext {

  val logger = LoggerFactory.getLogger(getClass)

  /**
    * Specification for the Game-Mutex-Object
    * Mutex => MongoDBObject(gameId -> id, playerId -> id)
    *
    * @param id
    * @param playerId
    * @return
    */

  override def setMutexForGame(id: String, playerId: String): Option[MutexStatus] = {

    val mutexObject = MongoDBObject("gameid" -> id)

    collectionMutex.collection.findOne(mutexObject).map { found =>
      found.get("playerid").asInstanceOf[String] == playerId match {
        case true => Option(MutexStatusCodes.AlreadyHolding)
        case false => Option(MutexStatusCodes.AquiredByAnother)
      }
    }.toList.headOption.getOrElse {
      collectionMutex.collection.save(MongoDBObject("gameid" -> id, "playerid" -> playerId))
      Option(MutexStatusCodes.AquiredSuccess)
    }

  }

  override def getById(id: String): Option[Game] = {

    val objectToSearch = MongoDBObject("gameid" -> id)

    collectionGames.collection.findOne(objectToSearch).map { document =>
      grater[Game].asObject(document)
    }
  }

  // playerId
  override def checkMutexForGame(id: String): Option[String] = {

    collectionMutex.collection.findOne(MongoDBObject("gameid" -> id)).map { document =>
     document.get("playerid").asInstanceOf[String]
    }

  }

  override def deleteMutexForGame(gameId: String): Option[Boolean] = {
    Option(collectionMutex.collection.findAndRemove(MongoDBObject("gameid" -> gameId)).isDefined)
  }

  override def releaseMutexFor(gameId: String, playerId: String): Option[Boolean] = {
    Option(collectionMutex.collection.findAndRemove(MongoDBObject("gameid" -> gameId, "playerid" -> playerId)).isDefined)
  }

  override def create(id: String): Option[Game] = {

    val newgame = Game(id,Set())
    val newgamedbobject = grater[Game].asDBObject(newgame)

    logger.info(s"Trying to create game: ${newgamedbobject}")

    Try(collectionGames.collection.save(newgamedbobject)).map { writeResult =>
      Some(newgame)
    }.getOrElse(None)
  }

  override def create(o:Game) :Option[Game] = {
    collectionGames.collection.save(grater[Game].asDBObject(o))
    Option(o)
  }

  override def delete(id: String): Option[Boolean] = {
    Option(collectionGames.collection.
      findAndRemove(MongoDBObject("gameid" -> id)).isDefined
    )
  }

  override def checkMutexForPlayer(gameId: String, playerId: String): Option[MutexStatus] = {
    val mutexObject = MongoDBObject("gameid" -> gameId)

    collectionMutex.collection.findOne(mutexObject).map { found =>
      found.get("playerid").asInstanceOf[String] == playerId match {
        case true => Option(MutexStatusCodes.AlreadyHolding)
        case false => Option(MutexStatusCodes.AquiredByAnother)
      }
    }.toList.headOption.getOrElse(None)
  }
}
