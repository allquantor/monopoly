package com.haw.monopoly.web


import com.haw.monopoly.data.DataModule
import com.haw.monopoly.data.repositories.{BoardRepository, GameRepository}
import com.softwaremill.macwire.MacwireMacros._

trait WebModule extends DataModule {


  lazy val boardsController = wire[BoardsController]

  lazy val gameController = wire[GamesController]


}
