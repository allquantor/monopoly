package com.haw.monopoly.data.repositories

import com.haw.monopoly.core.Broker

/**
  * Created by Ivan Morozov on 12/12/15.
  */
trait BrokersRepository {
  def create(brokerId:String): Option[Broker]

}
