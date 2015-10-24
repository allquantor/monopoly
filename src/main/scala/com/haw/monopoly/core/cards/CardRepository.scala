package com.haw.monopoly.core.cards

import com.haw.monopoly.core.cards.CardType.TypeOfMonopolyCard

/**
 * Created by Ivan Morozov on 24/10/15.
 */
trait CardRepository {

  import scala.util.Random._

  def all(limit: Option[Int] = None): Seq[Card]

  def removeFromDeck(card: Card): Seq[Card]

  def aTypeCard(deck: Seq[Card], t: TypeOfMonopolyCard): Card = {
    shuffle(deck.filter(_.cardType == t)).
      headOption.
      getOrElse(
        throw new Exception(s"$t cards empty")
      )
  }





}
