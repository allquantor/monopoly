package com.haw.monopoly.core.entities.game

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.haw.monopoly.core.player.PlayerGames

/**
  * Created by Ivan Morozov on 24/10/15.
  */

// components = Name -> Id
case class Game(gameid: String, uri:String, players: Set[PlayerGames],components:Components)


case class Components(game:String,dice:String,board:String,bank:String,broker:String,decks:String,events:String)

