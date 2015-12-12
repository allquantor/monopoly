package com.haw.monopoly.web

import com.haw.monopoly.core.Event
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
    val id = (scala.util.Random.nextInt(100000) + 1).toString
    eventRepository.create(Event(id, "-", "-", "-", null, -1, false))
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

  post("/:eventid/subscription") {

  }

}
