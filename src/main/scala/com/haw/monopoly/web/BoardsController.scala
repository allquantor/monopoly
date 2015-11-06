package com.haw.monopoly.web

import com.haw.monopoly.core.dice.Dice
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

/**
  * Created by Ivan Morozov on 24/10/15.
  */
class BoardsController extends ScalatraServlet with JacksonJsonSupport {
  override protected implicit def jsonFormats: Formats = DefaultFormats


  post(":gameid/players/:playerid/roll") {


    val gameId = params("gameid")
    val playerId = params("playerid")

    val reqBodyJson = parse(request.body)

    val dice1 = (reqBodyJson \ "roll1").extract[Dice]

    val dice2 = (reqBodyJson \ "roll2").extract[Dice]


    //todo: render some logic results


  }


  before() {
    contentType = formats("json")
  }

}
