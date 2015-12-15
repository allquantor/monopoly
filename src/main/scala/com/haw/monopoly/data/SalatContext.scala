package com.haw.monopoly.data

import com.haw.monopoly.core.entities.board.Board
import com.haw.monopoly.core.entities.game.{Components, Game}
import com.haw.monopoly.core.player._
import com.haw.monopoly.core.{Estate, Event, Point, Subscription}
import com.mongodb.DBObject
import com.mongodb.casbah.commons.Imports._
import com.novus.salat._
import com.novus.salat.json._
import com.novus.salat.transformers._




trait SalatContext {


  object PointTransformer extends CustomTransformer[Point, DBObject] {

    def serialize(p: Point): DBObject =
      MongoDBObject("type" -> "Point", "coordinates" -> List(p.longitude, p.latitude))

    def deserialize(o: DBObject): Point = {
      val coordinates = o.get("coordinates").asInstanceOf[java.util.List[Double]]
      Point(coordinates.get(0), coordinates.get(1))
    }

  }

  object PlaceTransformer extends CustomTransformer[Place, DBObject] {

    override def deserialize(b: DBObject): Place = {

      val pointId = b.get("id").asInstanceOf[String]
      val name = b.get("name").asInstanceOf[String]
      Place(pointId, name)
    }

    override def serialize(a: Place): DBObject = {
      MongoDBObject("id" -> a.id, "name" -> a.name)
    }

  }


  object BoardTransformer extends CustomTransformer[Board, DBObject] {

    override def deserialize(b: DBObject): Board = {

      val id = b.get("id").asInstanceOf[String]

      val playerList = b.get("player").asInstanceOf[Set[DBObject]].map(PlayerBoardTransformer.deserialize)
      Board(id, playerList)

    }

    override def serialize(a: Board): DBObject = {
      MongoDBObject("id" -> a.id, "player" -> a.player.map(PlayerBoardTransformer.serialize))
    }

  }


  object PlayerBoardTransformer extends CustomTransformer[PlayerBoards, DBObject] {

    override def deserialize(b: DBObject): PlayerBoards = {

      val id = b.get("id").asInstanceOf[String]

      val position = b.get("position").asInstanceOf[Int]

      val place = PlaceTransformer.deserialize(b.get("place").asInstanceOf[DBObject])

      PlayerBoards(id, place, position)
    }

    override def serialize(a: PlayerBoards): DBObject = {
      MongoDBObject("id" -> a.id, "position" -> a.position, "place" -> PlaceTransformer.serialize(a.place))
    }
  }

  object GameTransformer extends CustomTransformer[Game, DBObject] {

    override def deserialize(b: DBObject): Game = {
      val id = b.get("gameid").asInstanceOf[String]
      val playerGames = b.get("player").asInstanceOf[Set[DBObject]].map(PlayerGameTransformer.deserialize)
      val components = ComponentsTransformer.deserialize(b.get("components").asInstanceOf[DBObject])
      val uri = b.get("uri").asInstanceOf[String]
      Game(id, uri, playerGames, components)
    }

    override def serialize(a: Game): DBObject = {

      MongoDBObject(
        "gameid" -> a.gameid,
        "uri" -> a.uri,
        "player" -> a.players.map(PlayerGameTransformer.serialize),
        "components" -> ComponentsTransformer.serialize(a.components)
      )
    }
  }

  object PlayerGameTransformer extends CustomTransformer[PlayerGames, DBObject] {

    override def deserialize(b: DBObject): PlayerGames = {

      val id = b.get("id").asInstanceOf[String]
      val name = b.get("name").asInstanceOf[String]
      val uri = b.get("uri").asInstanceOf[String]
      val ready = b.get("ready").asInstanceOf[Boolean]

      PlayerGames(id, name, uri, ready)
    }

    override def serialize(a: PlayerGames): DBObject = {
      MongoDBObject(
        "id" -> a.id,
        "name" -> a.name,
        "uri" -> a.uri,
        "ready" -> a.ready
      )
    }
  }

  object EventTransformer extends CustomTransformer[Event, DBObject] {
    override def deserialize(b: DBObject): Event = {
      Event(
        b.get("id").asInstanceOf[String],
        b.get("_type").asInstanceOf[String],
        b.get("name").asInstanceOf[String],
        b.get("reason").asInstanceOf[String],
        b.get("ressource").asInstanceOf[String],
        PlayerEventTransformer.deserialize(b.get("player").asInstanceOf[DBObject])
      )
    }

    override def serialize(a: Event): DBObject = {
      MongoDBObject(
        "id" -> a.id,
        "_type" -> a._type,
        "name" -> a.name,
        "reason" -> a.reason,
        "ressource" -> a.resource,
        "player" -> PlayerEventTransformer.serialize(a.player)
      )
    }
  }

  object PlayerPositionTransformer extends CustomTransformer[PlayerPosition, DBObject] {
    override def deserialize(b: DBObject): PlayerPosition = {
      PlayerPosition(
        b.get("id").asInstanceOf[String],
        EventTransformer.deserialize(b.get("event").asInstanceOf[DBObject])
      )
    }

    override def serialize(a: PlayerPosition): DBObject = {
      MongoDBObject(
        "id" -> a.playerId,
        "event" -> EventTransformer.serialize(a.event)
      )
    }
  }

  object SubscriptionTransformer extends CustomTransformer[Subscription, DBObject] {

    override def deserialize(b: DBObject): Subscription = {
      Subscription(
        b.get("id").asInstanceOf[String],
        b.get("gameid").asInstanceOf[String],
        b.get("uri").asInstanceOf[String],
        b.get("callbackuri").asInstanceOf[String],
        grater[Event].asObject(b.get("event").asInstanceOf[DBObject])
      )
    }

    override def serialize(a: Subscription): DBObject = {
      MongoDBObject(
        "id" -> a.id,
        "gameid" -> a.gameid,
        "uri" -> a.uri,
        "callbackuri" -> a.callbackuri,
        "event" -> grater[Event].asDBObject(a.event)
      )
    }
  }


  object PlayerEventTransformer extends CustomTransformer[PlayerEvents, DBObject] {

    override def deserialize(b: DBObject): PlayerEvents = {

      val id = b.get("id").asInstanceOf[String]
      val name = b.get("name").asInstanceOf[String]
      val reason = b.get("reason").asInstanceOf[String]
      val uri = b.get("resource").asInstanceOf[String]
      val place = PlaceTransformer.deserialize(b.get("place").asInstanceOf[DBObject])
      val position = b.get("position").asInstanceOf[Int]
      val ready = b.get("ready").asInstanceOf[Boolean]
      PlayerEvents(id, name, uri, place, position, ready)
    }

    override def serialize(a: PlayerEvents): DBObject = {
      MongoDBObject(
        "id" -> a.id,
        "name" -> a.name,
        "uri" -> a.uri,
        "place" -> PlaceTransformer.serialize(a.place),
        "position" -> a.position,
        "ready" -> a.ready
      )
    }

  }

  object ComponentsTransformer extends CustomTransformer[Components, DBObject] {
    override def deserialize(b: DBObject): Components = {

      Components(
        b.get("game").asInstanceOf[String],
        b.get("dice").asInstanceOf[String],
        b.get("board").asInstanceOf[String],
        b.get("bank").asInstanceOf[String],
        b.get("broker").asInstanceOf[String],
        b.get("decks").asInstanceOf[String],
        b.get("events").asInstanceOf[String]
      )
    }

    override def serialize(a: Components): DBObject = {
      MongoDBObject(
        "game" -> a.game,
        "dice" -> a.dice,
        "board" -> a.board,
        "bank" -> a.bank,
        "broker" -> a.broker,
        "decks" -> a.decks,
        "events" -> a.events
      )
    }
  }


  object EstateTransformer extends CustomTransformer[Estate, DBObject] {
    import scala.collection.JavaConverters._

    override def deserialize(b: DBObject): Estate = {

      val rent = b.getAs[java.util.List[Int]]("rent").get.asScala.toArray
      val cost =  b.getAs[java.util.List[Int]]("cost").get.asScala.toArray

      Estate(
        b.get("id").asInstanceOf[String],
        b.get("place").asInstanceOf[String],
        b.get("owner").asInstanceOf[String],
        b.get("value").asInstanceOf[Int],
        rent,
        cost,
        b.get("houses").asInstanceOf[Int]
      )
    }

    override def serialize(a: Estate): DBObject = {
      MongoDBObject(
        "id" -> a.id,
        "place" -> a.place,
        "owner" -> a.owner,
        "value" -> a.value,
        "rent" -> a.rent,
        "cost" -> a.cost,
        "houses" -> a.houses
      )
    }


  }


  implicit val salatContext = new Context() {
    override val name = "custom_salat_context"
    override val typeHintStrategy = StringTypeHintStrategy(TypeHintFrequency.WhenNecessary, "_t")
    override val jsonConfig = JSONConfig(objectIdStrategy = StringObjectIdStrategy)

    registerCustomTransformer(PointTransformer)
    registerCustomTransformer(PlayerBoardTransformer)
    registerCustomTransformer(PlaceTransformer)
    registerCustomTransformer(BoardTransformer)
    registerCustomTransformer(GameTransformer)
    registerCustomTransformer(EventTransformer)
    registerCustomTransformer(PlayerPositionTransformer)
    registerCustomTransformer(SubscriptionTransformer)
    registerCustomTransformer(PlayerEventTransformer)
    registerCustomTransformer(ComponentsTransformer)
    registerCustomTransformer(EstateTransformer)


    // registerGlobalKeyOverride(remapThis = "id", toThisInstead = "_id")
  }


}
