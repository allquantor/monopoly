package com.haw.monopoly.data

import com.haw.monopoly.core.cards.{Card, CardRepository}
import com.haw.monopoly.util.MonoLogger
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.commons.MongoDBObject
import com.novus.salat._

/**
 * Created by Ivan Morozov on 24/10/15.
 */
class MongoCardRepository(collection: MongoCollection) extends CardRepository with MonoLogger {

  override def all(limit: Option[Int]): Seq[Card] = {
    collection.find
      .limit(limit.getOrElse(0))
      .toList.map(o => grater[Card].asObject(o))
  }

  override def removeFromDeck(card: Card): Seq[Card] = {
    val writeResult = collection -= MongoDBObject("objectId" -> card.objectId)
    log.info(s"Deleted card $card")
    all()
  }
}
