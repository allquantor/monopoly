package com.haw.monopoly.rmi

import java.rmi.{Remote, RemoteException}


/**
 * Created by Ivan Morozov on 04/11/15.
 */
trait DiceRMI extends Remote {

  @throws(classOf[RemoteException])
  def roll(): Roll

}
