package com.haw.monopoly.data

import com.haw.monopoly.data.collections._
import com.haw.monopoly.data.repositories._
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


  lazy val boardsCollection:BoardCollection = BoardCollection(db(config.getString("mongo.collections.boards")))
  lazy val gameCollection:GamesCollection = GamesCollection(db(config.getString("mongo.collections.games")))
  lazy val gameMutexCollection:MutexCollection = MutexCollection(db(config.getString("mongo.collections.mutex")))
  lazy val playerCollection:PlayerCollection = PlayerCollection(db(config.getString("mongo.collections.player")))
  lazy val eventCollection:EventCollection = EventCollection(db(config.getString("mongo.collections.event")))
  lazy val subscriptionCollection:SubscriptionCollection = SubscriptionCollection(db(config.getString("mongo.collections.subscription")))
  lazy val brokerCollection:BrokerCollection = BrokerCollection(db(config.getString("mongo.collections.broker")))
  lazy val estateCollection:EstateCollection = EstateCollection(db(config.getString("mongo.collections.estate")))


  lazy val boardsRepository: BoardRepository  = wire[MongoBoardRepository]
  lazy val gameRepository: GameRepository  = wire[MongoGameRepository]
  lazy val playerRepository : PlayersPrivateRepository = wire[MongoPlayersPrivateRepository]
  lazy val eventRepository : EventRepository = wire[MongoEventRepository]
  lazy val brokerRespository : BrokersRepository = wire[MongoBrokerRepository]
  lazy val estateRepository : EstateRepository = wire[MongoEstateRepository]


}
