package com.haw.monopoly.data

import com.haw.monopoly.core.Point
import com.haw.monopoly.core.entities.board.Board
import com.haw.monopoly.core.player.{Place, Player}
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
      val playerList = b.get("player").asInstanceOf[Set[DBObject]].map(PlayerTransformer.deserialize)
      Board(id, playerList)

    }

    override def serialize(a: Board): DBObject = {
      MongoDBObject("id" -> a.id, "player" -> a.player.map(PlayerTransformer.serialize))
    }

  }


  object PlayerTransformer extends CustomTransformer[Player, DBObject] {


    override def deserialize(b: DBObject): Player = {
      val id = b.get("id").asInstanceOf[String]
      val position = b.get("position").asInstanceOf[Int]
      val place = PlaceTransformer.deserialize(b.get("place").asInstanceOf[DBObject])
      Player(id, place, position)
    }

    override def serialize(a: Player): DBObject = {
      MongoDBObject("id" -> a.id, "position" -> a.position, "place" -> PlaceTransformer.serialize(a.place))
    }
  }


  implicit val salatContext = new Context() {
    override val name = "custom_salat_context"
    override val typeHintStrategy = StringTypeHintStrategy(TypeHintFrequency.WhenNecessary, "_t")
    override val jsonConfig = JSONConfig(objectIdStrategy = StringObjectIdStrategy)

    registerCustomTransformer(PointTransformer)
    registerCustomTransformer(PlayerTransformer)
    registerCustomTransformer(BoardTransformer)
    registerCustomTransformer(PlaceTransformer)

   // registerGlobalKeyOverride(remapThis = "id", toThisInstead = "_id")
  }


}
