package com.haw.monopoly.web

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport

/**
 * Created by Ivan Morozov on 24/10/15.
 */
class GameController extends ScalatraServlet with JacksonJsonSupport {
  override protected implicit def jsonFormats: Formats = DefaultFormats


  post("/") {

  }

  get("/") {

  }

  get("/:gameid") {

  }

  get("/games/:gameid/players") {

  }


  before() {
    contentType = formats("json")
  }

}
