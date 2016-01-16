package com.haw.monopoly.data.repositories

import com.haw.monopoly.core.Location
import com.haw.monopoly.core.entities.game.Game
import com.haw.monopoly.core.player.PlayerBoards
import com.haw.monopoly.data.repositories.MutexStatusCodes.MutexStatus

/**
  * Created by Ivan Morozov on 24/10/15.
  */
trait GameRepository {
  def checkMutexForPlayer(gameId: String, playerId: String): Option[MutexStatus]

  def releaseMutexFor(gameId: String, playerId: String): Option[Boolean]

  def deleteMutexForGame(gameId: String): Option[Boolean]

  def setMutexForGame(id: String, playerId: String): Option[MutexStatus]

  def getById(id:String):Option[Game]

  def checkMutexForGame(id:String):Option[String]

  def create(id:String):Option[Game]

  def create(game:Game):Option[Game]

  def delete(id:String):Option[Boolean]

  def getAllGames: List[Game]

}

object MutexStatusCodes extends Enumeration {

  type MutexStatus = Value
  val AlreadyHolding, AquiredSuccess, AquiredByAnother = Value

  def statusToCode(s: MutexStatus): Int = {
    s match {
      case AlreadyHolding => 200
      case AquiredSuccess => 201
      case AquiredByAnother => 409
    }
  }
}



