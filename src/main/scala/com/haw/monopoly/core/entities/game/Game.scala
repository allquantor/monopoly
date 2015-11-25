package com.haw.monopoly.core.entities.game

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.haw.monopoly.core.player.PlayerGames

/**
  * Created by Ivan Morozov on 24/10/15.
  */

// components = Name -> Id
@JsonIgnoreProperties(Array("components"))
case class Game(gameid: String, player: Set[PlayerGames])


