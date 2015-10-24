package com.haw.monopoly.core.cards

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.haw.monopoly.core.cards.CardType.TypeOfMonopolyCard
import org.bson.types.ObjectId

/**
 * Created by Ivan Morozov on 22/10/15.
 */


@JsonIgnoreProperties(value = Array("objectId,cardType"))
case class Card(objectId: ObjectId, cardType: TypeOfMonopolyCard, name: String, text: String)


object CardType extends Enumeration {
  type TypeOfMonopolyCard = Value
  val Community, Chance = Value
}












