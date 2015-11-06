package com.haw.monopoly.core.game

import com.haw.monopoly.core.Location
import com.haw.monopoly.core.game.MutexStatusCodes.MutexStatus

/**
  * Created by Ivan Morozov on 24/10/15.
  */
trait GameRepository {

  def all(limit: Option[Int] = None): Seq[Location]

  def byId(id: org.bson.types.ObjectId): Option[Location]

  def byNameFragment(name: String, limit: Option[Int] = None): Seq[Location]

  def byTextPhrase(phrase: String, limit: Option[Int] = None): Seq[Location]


  def putMutexForGame(id: String, playerId: String): Option[MutexStatus]


}

object MutexStatusCodes extends Enumeration {
  // todo to enum
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



