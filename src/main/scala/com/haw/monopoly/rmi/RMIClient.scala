package com.haw.monopoly.rmi

import java.rmi.registry.LocateRegistry

/**
 * Created by Ivan Morozov on 04/11/15.
 */
object RMIClient extends App {

  try {

    val registry = LocateRegistry getRegistry("localhost", 2000)

    val stub = registry.lookup("dicermiservice").asInstanceOf[DiceRMI]
    println(stub.roll())

  } catch {
    case e: Exception => e printStackTrace
  }

}
