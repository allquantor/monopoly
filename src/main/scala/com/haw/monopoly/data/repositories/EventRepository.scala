package com.haw.monopoly.data.repositories

import com.haw.monopoly.core.{Subscription, Event}

/**
  * Created by Ivan Morozov on 10/12/15.
  */
trait EventRepository {
  def findByName(name: String): Option[Event]

  def findToId(id: String): Option[Event]

  def get()

  def create(e: Event)

  def delete()


  def createSubscription(s:Subscription):Option[Subscription]

}
