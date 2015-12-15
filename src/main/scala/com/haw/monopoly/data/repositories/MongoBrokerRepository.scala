package com.haw.monopoly.data.repositories

import com.haw.monopoly.core.Broker
import com.haw.monopoly.data.SalatContext
import com.haw.monopoly.data.collections.BrokerCollection
import com.mongodb.casbah.commons.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import com.novus.salat._

/**
  * Created by Ivan Morozov on 15/12/15.
  */
class MongoBrokerRepository(brokerCollection: BrokerCollection) extends BrokersRepository with SalatContext{

  override def create(brokerId: String): Option[Broker] = {
    val nb = Broker(brokerId)
    brokerCollection.collection.save(
      grater[Broker].asDBObject(nb)
    )
    Some(nb)
  }

}
