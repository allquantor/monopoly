package com.haw.monopoly.core

import com.haw.monopoly.core.player.Place

/**
  * Created by Ivan Morozov on 10/12/15.
  */
case class Event(id:String,_type:String,name:String,uri:String,place: Place,position:Int,ready:Boolean)
