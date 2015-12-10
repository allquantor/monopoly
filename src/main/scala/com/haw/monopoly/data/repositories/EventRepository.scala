package com.haw.monopoly.data.repositories

import com.haw.monopoly.core.Event

/**
  * Created by Ivan Morozov on 10/12/15.
  */
trait EventRepository {

  def create(e:Event)

}
