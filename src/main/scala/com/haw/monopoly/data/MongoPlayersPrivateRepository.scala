package com.haw.monopoly.data

import com.haw.monopoly.core.Event
import com.haw.monopoly.core.entities.board.Board
import com.haw.monopoly.core.player.PlayerPosition
import com.haw.monopoly.data.collections.PlayerCollection
import com.haw.monopoly.data.repositories.PlayersPrivateRepository
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.commons.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import com.novus.salat._

/**
  * Created by Ivan Morozov on 10/12/15.
  */
class MongoPlayersPrivateRepository(playerCollection:PlayerCollection) extends PlayersPrivateRepository with SalatContext {

  override def setReady(playerId:String,status:Boolean): Option[PlayerPosition] = {
    playerCollection.collection.findOne(MongoDBObject("id" -> playerId)).map { document =>
      val currentPosition = grater[PlayerPosition].asObject(document)
      currentPosition.copy(event = currentPosition.event.copy(ready = status))
    }.map { newposition =>
      playerCollection.collection.findAndRemove(MongoDBObject("id" -> playerId))
      playerCollection.collection.save(grater[PlayerPosition].asDBObject(newposition))
      newposition
    }
  }



  override def updateCurrentState(playerId:String,newevent:Event): Option[PlayerPosition] = {
    playerCollection.collection.findOne(MongoDBObject("id" -> playerId)).map { document =>
      val currentPosition = grater[PlayerPosition].asObject(document)
      currentPosition.copy(event = newevent)
    }.map { newposition =>
      playerCollection.collection.findAndRemove(MongoDBObject("id" -> playerId))
      playerCollection.collection.save(grater[PlayerPosition].asDBObject(newposition))
      newposition
    }
  }

}
