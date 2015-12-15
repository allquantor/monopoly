package com.haw.monopoly.core

/**
  * Created by Ivan Morozov on 12/12/15.
  */
case class Broker(id:String)

case class Estate(id:String,place:String,owner:String,value:Int,rent:Array[Int],cost:Array[Int],houses:Int)
