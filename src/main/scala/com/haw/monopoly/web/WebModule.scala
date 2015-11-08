package com.haw.monopoly.web

import com.haw.monopoly.core.LocationRepository
import com.haw.monopoly.data.DataModule
import com.softwaremill.macwire.MacwireMacros._

trait WebModule extends DataModule{

  def locationRepository: LocationRepository

  lazy val locationController = wire[LocationController]

  lazy val boardsController = wire[BoardsController]


}
