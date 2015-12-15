package com.haw.monopoly.data

import com.mongodb.casbah.MongoCollection

/**
  * Created by Ivan Morozov on 23/11/15.
  */
package object collections {

  case class BoardCollection(collection: MongoCollection)

  case class GamesCollection(collection: MongoCollection)

  case class MutexCollection(collection: MongoCollection)

  case class PlayerCollection(collection: MongoCollection)

  case class EventCollection(collection: MongoCollection)

  case class SubscriptionCollection(collection: MongoCollection)

  case class BrokerCollection(collection: MongoCollection)

  case class EstateCollection(collection: MongoCollection)

}
