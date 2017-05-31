package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.core.Resource;

public class VictoryPointsFromCardsEffect extends Effect {

	private CardType cardType;

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

}
