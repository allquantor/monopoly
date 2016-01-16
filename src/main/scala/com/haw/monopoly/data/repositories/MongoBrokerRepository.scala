package com.haw.monopoly.data.repositories

import com.haw.monopoly.core.{Broker, NewPlace}
import com.haw.monopoly.data.SalatContext
import com.haw.monopoly.data.collections.BrokerCollection
import com.mongodb.casbah.commons.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import com.novus.salat._

/**
  * Created by Ivan Morozov on 15/12/15.
  */
class MongoBrokerRepository(brokerCollection: BrokerCollection, estateRepository: EstateRepository) extends BrokersRepository with SalatContext {

  val estatesCount = 50

  override def create(gameId: String): Option[Broker] = {


    val startEstate = NewPlace(1,
      "Start",
      "NO-OWNER",
      -1,
      Array(),
      Array(),
      0
    )

    val places = (2 to 50).map { id =>
      NewPlace(id,
        s"Place-$id",
        "NO-OWNER",
        scala.util.Random.nextInt(500) + 100,
        Array(),
        Array(),
        0)
    }.toList

    val nb = Broker(gameId,places)
    brokerCollection.collection.save(
      grater[Broker].asDBObject(nb)
    )
    Some(nb)
  }

  override def findById(brokerId: String): Option[Broker] = {
    brokerCollection.collection.findOne(MongoDBObject("id" -> brokerId)).map(d => grater[Broker].asObject(d))
  }
}
