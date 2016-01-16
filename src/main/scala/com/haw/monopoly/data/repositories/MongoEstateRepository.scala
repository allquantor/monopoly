package com.haw.monopoly.data.repositories

import com.haw.monopoly.core.Estate
import com.haw.monopoly.data.SalatContext
import com.haw.monopoly.data.collections.EstateCollection
import com.mongodb.casbah.commons.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import com.novus.salat._

/**
  * Created by Ivan Morozov on 15/12/15.
  */
class MongoEstateRepository(collection:EstateCollection) extends EstateRepository with SalatContext{
  override def create(e: Estate, gameId: String): Option[Estate] = {
    collection.collection.save (grater[Estate].asDBObject(e) ++ MongoDBObject("gameid" -> gameId))
    Some(e)
  }

  override def findByPlaceId(gameid: String, placeid: String): Option[Estate] = {
    collection.collection.findOne(
      MongoDBObject(
        "gameid" -> gameid,
        "id" -> placeid
      )
    ).map( document =>
      EstateTransformer.deserialize(document)
    )
  }

  override def updateEstate(id: String, gameid: String, playerid: String): Option[Estate] = {
    collection.collection.findOne( MongoDBObject(
      "gameid" -> gameid,
      "id" -> id)
    ).map {EstateTransformer.deserialize}.map {estate => estate.copy(owner = playerid)}.map { newstate =>
      collection.collection.findAndRemove( MongoDBObject(
        "gameid" -> gameid,
        "id" -> id)
      )
      val obj = grater[Estate].asDBObject(newstate)
      collection.collection.save(obj)
      newstate
    }
  }
}
