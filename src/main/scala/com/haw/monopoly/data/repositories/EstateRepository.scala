package com.haw.monopoly.data.repositories

import com.haw.monopoly.core.Estate
import org.json4s.JValue

/**
  * Created by Ivan Morozov on 12/12/15.
  */
trait EstateRepository {

  def updateEstate(id: String, gameid: String, playerid: String): Option[Estate]

  def findById(gameid: String, placeid: String): Option[Estate]

  def create(e:Estate,gameId:String):Option[Estate]

}
