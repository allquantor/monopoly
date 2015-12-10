package com.haw.monopoly.data.repositories

import com.haw.monopoly.core.Event
import com.haw.monopoly.core.player.PlayerPosition

/**
  * Created by Ivan Morozov on 10/12/15.
  */
trait PlayersPrivateRepository {
  def updateCurrentState(playerId:String,event: Event) : Option[PlayerPosition]
  def setReady(playerId:String,status:Boolean) : Option[PlayerPosition]

}
