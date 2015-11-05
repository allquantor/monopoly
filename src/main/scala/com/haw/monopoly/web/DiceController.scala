package com.haw.monopoly.web

import com.haw.monopoly.core.dice.Dice
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

/**
 * Created by Ivan Morozov on 15/10/15.
 */
class DiceController() extends ScalatraServlet with JacksonJsonSupport {

  override protected implicit def jsonFormats: Formats = DefaultFormats //+ Serializers.objectId

  get("/") {
    //println("ICH BIN HIER!!!")
    Dice(scala.util.Random.nextInt(6) + 1)
  }


  before() {
    contentType = formats("json")
  }


}
