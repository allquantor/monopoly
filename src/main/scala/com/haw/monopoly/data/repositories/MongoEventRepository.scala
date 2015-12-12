package com.haw.monopoly.data.repositories

import com.haw.monopoly.core.Event
import com.haw.monopoly.core.entities.board.Board
import com.haw.monopoly.data.SalatContext
import com.haw.monopoly.data.collections.EventCollection
import com.novus.salat._

/**
  * Created by Ivan Morozov on 10/12/15.
  */
class MongoEventRepository(eventCollection:EventCollection) extends EventRepository with SalatContext{
  override def create(e: Event): Unit = {
    eventCollection.collection.save( grater[Event].asDBObject(e) )
  }

  override def delete(): Unit = ???

  override def get(): Unit = ???

  override def findToId(id: String): Option[Event] = ???
}
