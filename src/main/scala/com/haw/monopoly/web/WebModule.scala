package com.haw.monopoly.web

import com.softwaremill.macwire.MacwireMacros._

trait WebModule {


  lazy val diceController = wire[DiceController]

  lazy val decksController = wire[DecksController]




}
