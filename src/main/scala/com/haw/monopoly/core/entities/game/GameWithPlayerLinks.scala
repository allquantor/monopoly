package com.haw.monopoly.core.entities.game

import com.haw.monopoly.core.player.PlayerGames

/**
  * Created by Ivan Morozov on 16/01/16.
  */
case class GameWithPlayerLinks(gameid: String, uri:String, players: Set[String],components:Components)

