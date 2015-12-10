package com.haw.monopoly.data

import com.haw.monopoly.core.entities.board.Board
import com.haw.monopoly.core.entities.game.Game
import com.haw.monopoly.core.player.{Place, PlayerBoards, PlayerGames, PlayerPosition}
import com.haw.monopoly.core.{Event, Point}
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

      Game(id, playerGames)
    }

    override def serialize(a: Game): DBObject = {

      MongoDBObject(
        "gameid" -> a.gameid,
        "player" -> a.player.map(PlayerGameTransformer.serialize)
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
        b.get("uri").asInstanceOf[String],
        PlaceTransformer.deserialize(b.get("place").asInstanceOf[DBObject]),
        b.get("position").asInstanceOf[Int],
        b.get("ready").asInstanceOf[Boolean]
      )
    }

    override def serialize(a: Event): DBObject = {
      MongoDBObject(
        "id" -> a.id,
        "_type" -> a._type,
        "name" -> a.name,
        "uri" -> a.uri,
        "place" -> PlaceTransformer.serialize(a.place),
        "position" -> a.position,
        "ready" -> a.ready
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


    // registerGlobalKeyOverride(remapThis = "id", toThisInstead = "_id")
  }


}
