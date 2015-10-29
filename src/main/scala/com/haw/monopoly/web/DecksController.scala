package com.haw.monopoly.web

import com.haw.monopoly.core.dice.{CardRepository, CardType}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

/**
 * Created by Ivan Morozov on 22/10/15.
 */
class DecksController() extends ScalatraServlet with JacksonJsonSupport {

  import CardRepository.All_Cards

  import scala.util.Random._


  get("/:gameid/chance") {
    val id = params("gameid")
    // pick the right game
    // render random card
    shuffle(
      All_Cards.filter(_.cardType == CardType.Chance)
    ).headOption.map { c =>
      /*All_Cards = */ All_Cards - c
      c
    }.getOrElse( """{"message": "EmptyStack"}""")


  }

  get("/:gameid/community") {
    val id = params("gameid")

    shuffle(
      All_Cards.filter(_.cardType == CardType.Community)
    ).headOption.map { c =>
      /*All_Cards = */ All_Cards - c
      c
    }.getOrElse( """{"message": "EmptyStack"}""")

  }


  before() {
    contentType = formats("json")
  }

  override protected implicit def jsonFormats: Formats = DefaultFormats
}


