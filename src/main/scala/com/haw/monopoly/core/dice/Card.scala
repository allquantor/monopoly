package com.haw.monopoly.core.dice

import com.fasterxml.jackson.annotation.{JsonIgnore, JsonIgnoreProperties}
import com.haw.monopoly.core.dice.CardType.CardType


/**
 * Created by Ivan Morozov on 29/10/15.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
case class Card(name: String, text: String, @JsonIgnore cardType:CardType)

object CardType extends Enumeration {
  type CardType = Value
  val Community, Chance = Value
}


object CardRepository {
  @volatile var All_Cards = Set (
    Card("knast","knastFrei",CardType.Chance),
    Card("knast","knastRein",CardType.Community)
  )
}


