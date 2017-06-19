package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.core.Resource;

public class VictoryPointsFromCardsEffect extends Effect {

	private CardType cardType;
	
	public VictoryPointsFromCardsEffect(CardType cardType) {
		this.cardType = cardType;
	}

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		if (cardType == CardType.BUILDING) {
			// TODO: implement
			// Remove a number of victory points equal to the sum of the costs of every owned building card
		} else {
			effectResolutor.addResources(new Resource(cardType.toResourceType(),
					-effectResolutor.getResourceAmount(cardType.toResourceType())));
		}
	}
	
	@Override
	public String toString() {
		if (cardType == CardType.BUILDING) {
			return "At the end of the game, you lose 1 Victory Point for every wood and stone on your Building Cards’ costs";
		} else {
			return "At the end of the game, you don’t score Victory Points for any of your" + cardType.toString() + " cards";
		}
	}

}
