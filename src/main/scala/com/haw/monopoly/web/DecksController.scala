package com.haw.monopoly.web

import com.haw.monopoly.core.cards.CardRepository
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

/**
 * Created by Ivan Morozov on 22/10/15.
 */
class DecksController(cardRepository: CardRepository) extends ScalatraServlet with JacksonJsonSupport {
  override protected implicit def jsonFormats: Formats = DefaultFormats


  get("/:gameid/chance") {
    val id = params("gameid")

    // pick the right game
    // render random card
  }

  get("/:gameid/community") {
    scala.util.Random.
      shuffle(CardRepository.Community_Cards).head
  }


  before() {
    contentType = formats("json")
  }

}


