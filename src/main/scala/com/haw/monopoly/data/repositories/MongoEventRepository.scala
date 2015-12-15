package com.haw.monopoly.data.repositories

import com.haw.monopoly.core.{Subscription, Event}
import com.haw.monopoly.core.entities.board.Board
import com.haw.monopoly.data.SalatContext
import com.haw.monopoly.data.collections.{SubscriptionCollection, EventCollection}
import com.novus.salat._
import com.mongodb.casbah.commons.Imports._
import com.mongodb.casbah.commons.MongoDBObject



/**
  * Created by Ivan Morozov on 10/12/15.
  */
class MongoEventRepository(eventCollection:EventCollection,subscriptionCollection : SubscriptionCollection) extends EventRepository with SalatContext{

  override def create(e: Event): Unit = {
    eventCollection.collection.save( grater[Event].asDBObject(e) )
  }


  override def delete(): Unit = ???

  override def get(): Unit = ???

  override def findToId(id: String): Option[Event] = ???

  override def createSubscription(s:Subscription): Option[Subscription] = {
    subscriptionCollection.collection.save(grater[Subscription].asDBObject(s))
    Some(s)
  }

  override def findByName(name: String): Option[Event] = {
    eventCollection.collection.findOne(MongoDBObject("name" -> name)).map { document =>
      grater[Event].asObject(document)
    }
  }
}
