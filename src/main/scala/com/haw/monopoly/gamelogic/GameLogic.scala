package com.haw.monopoly.gamelogic

import com.haw.monopoly.GameConfigs
import dispatch.Defaults._
import dispatch._

import scala.concurrent.Future


/**
  * Created by Ivan Morozov on 05/11/15.
  */
object GameLogic extends GameConfigs {


  val extendRessource = {
    (x: String, t: String) => s"$x/$t"
  }


  def registerGame(req: Req): Future[String] = {
    Http(req).either.map {
      case Right(response) => response.getResponseBody
      case Left(e: Throwable) => ""
    }
  }


  def parseGameId(json: String): String = {
    //throw new NotImplementedError("implement this")
    "dummy"
  }


  def registerPlayer(playerName: String, playerUri: String, gameId: String) = {

    val newhost = extendRessource(extendRessource(ourHost, gameId), ourId)

    val req = host(newhost, ourPort).PUT.addQueryParameter("playername", playerName).
      addQueryParameter("playeruri", playerUri)

    val res = Http(req).either.map {
      case Right(response) => response
      case Left(e: Throwable) => throw e
    }

  }

}
