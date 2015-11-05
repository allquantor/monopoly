package com.haw.monopoly.rmi

import java.rmi.RemoteException
import java.rmi.registry.LocateRegistry
import java.rmi.server.UnicastRemoteObject

/**
 * Created by Ivan Morozov on 04/11/15.
 */

case class RMIServer() extends DiceRMI {
  @throws(classOf[RemoteException])
  override def roll(): Roll = Roll(scala.util.Random.nextInt(6) + 1)
}


object RMIServer extends App {

  try {

    LocateRegistry.createRegistry(2000)
    val server: RMIServer = RMIServer()
    val stub = UnicastRemoteObject.exportObject(server, 0).asInstanceOf[DiceRMI]
    val registry = LocateRegistry.getRegistry(2000)

    registry.rebind("dicermiservice", stub)

    println("Server ready")
  }
  catch {
    case e: Exception => e printStackTrace
  }


}
