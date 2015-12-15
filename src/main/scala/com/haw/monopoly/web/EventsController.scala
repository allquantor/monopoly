package com.haw.monopoly.web

import com.haw.monopoly.core.{Subscription, Event}
import com.haw.monopoly.core.player.{PlayerEvents, Place}
import com.haw.monopoly.data.repositories.EventRepository
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

/**
  * Created by Ivan Morozov on 10/12/15.
  */
class EventsController(eventRepository: EventRepository) extends ScalatraServlet with JacksonJsonSupport {

  override protected implicit def jsonFormats: Formats = DefaultFormats

  post("/") {
    val gameid = params("gameid")
    val id = (scala.util.Random.nextInt(100000) + 1).toString

    val _type = ((parse(request.body)) \ "type").extract[String]
    val name = ((parse(request.body)) \ "name").extract[String]
    val reason = ((parse(request.body)) \ "reason").extract[String]
    val resource = ((parse(request.body)) \ "resource").extract[String]
    val player = ((parse(request.body)) \ "player").extract[PlayerEvents]
    val event = Event(id,_type,name,reason,resource,player)

    eventRepository.create(event)
    id
  }

  get("/") {
    eventRepository.get()
  }

  get("/:eventid") {
    val id = params("eventid")
    eventRepository.findToId(id).getOrElse(status_=(404))
  }

  delete("/") {
    val id = (scala.util.Random.nextInt(100000) + 1).toString
    eventRepository.delete()
  }

  post("/subscriptions") {
    val gameid = ((parse(request.body)) \ "gameid").extract[String]
    val uri = ((parse(request.body)) \ "uri").extract[String]
    val callbackUri = ((parse(request.body)) \ "callbackuri").extract[String]

    val _event = ((parse(request.body)) \ "event")

    val name = (_event \ "name").extract[String]

    val eventObj = eventRepository.findByName(name).map { event =>
      val newsubsc = Subscription(event.id,gameid,uri,callbackUri,event)
      eventRepository.createSubscription(newsubsc)
    }.getOrElse(status_=(503))

  }

}
