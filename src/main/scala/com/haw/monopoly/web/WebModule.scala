package com.haw.monopoly.web

import com.haw.monopoly.core.LocationRepository
import com.softwaremill.macwire.MacwireMacros._

trait WebModule {

  def locationRepository: LocationRepository

  lazy val locationController = wire[LocationController]

  lazy val diceController = wire[DiceController]

  lazy val decksController = wire[DecksController]

  lazy val gameController = wire[GameController]


}
