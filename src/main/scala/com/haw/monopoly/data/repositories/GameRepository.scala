package com.haw.monopoly.data.repositories

import com.haw.monopoly.core.Location
import com.haw.monopoly.data.repositories.MutexStatusCodes.MutexStatus

/**
  * Created by Ivan Morozov on 24/10/15.
  */
trait GameRepository {

  def all(limit: Option[Int] = None): Seq[Location]

  def byId(id: org.bson.types.ObjectId): Option[Location]

  def byNameFragment(name: String, limit: Option[Int] = None): Seq[Location]

  def byTextPhrase(phrase: String, limit: Option[Int] = None): Seq[Location]


  def setMutexForGame(id: String, playerId: String): Option[MutexStatus]


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



