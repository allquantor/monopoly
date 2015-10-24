package com.haw.monopoly.core.game

import com.haw.monopoly.core.Location

/**
 * Created by Ivan Morozov on 24/10/15.
 */
trait GameRepository {

  def all(limit: Option[Int] = None): Seq[Location]

  def byId(id: org.bson.types.ObjectId): Option[Location]

  def byNameFragment(name: String, limit: Option[Int] = None): Seq[Location]

  def byTextPhrase(phrase: String, limit: Option[Int] = None): Seq[Location]


}
