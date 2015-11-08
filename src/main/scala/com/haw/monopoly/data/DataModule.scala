package com.haw.monopoly.data

import com.haw.monopoly.core.LocationRepository
import com.haw.monopoly.data.repositories.BoardRepository
import com.mongodb.casbah.MongoClient
import com.softwaremill.macwire.MacwireMacros._
import com.typesafe.config.Config
import org.slf4j.LoggerFactory

trait DataModule {

  val config: Config

  lazy val mongo = {
    val host = config.getString("mongo.host")
    val port = config.getInt("mongo.port")

    val logger = LoggerFactory.getLogger(classOf[DataModule])
    logger.info("Mongo client: host={} port={}", host, port)

    MongoClient(host, port)
  }

  lazy val db = mongo(config.getString("mongo.db"))


  lazy val boardsCollection = db(config.getString("mongo.collections.boards"))
  //lazy val locationCollection = db(config.getString("mongo.collections.locations"))

  //lazy val locationRepository: LocationRepository = wire[MongoLocationRepository]
  lazy val boardsRepository: BoardRepository  = wire[MongoBoardRepository]


}
