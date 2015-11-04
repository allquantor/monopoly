package com.haw.monopoly.core.player

import org.bson.types.ObjectId

/**
 * Created by Ivan Morozov on 24/10/15.
 */
case class Player(id: ObjectId, name: String, rmi: String, ready: Boolean)
