package com.haw.monopoly.core.game

import com.haw.monopoly.core.player.Player
import org.bson.types.ObjectId

/**
 * Created by Ivan Morozov on 24/10/15.
 */
case class Game(id: ObjectId, players: List[Player])


