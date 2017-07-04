package it.polimi.ingsw.LM45.model.effects;

import java.util.Arrays;

import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;

public class VictoryPointsFromCardsEffect extends Effect {

	private static final long serialVersionUID = 1L;

	private CardType cardType;

	public VictoryPointsFromCardsEffect(CardType cardType) {
		this.cardType = cardType;
	}

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		if (cardType == CardType.BUILDING) {
			// Remove a number of victory points equal to the sum of the costs of every owned building card (only wood and stone costs though)
			Resource[] totalCost = effectResolutor.getCardsTotalCost(cardType);
			int costSum = Arrays.stream(totalCost)
					.filter(resource -> resource.getResourceType() == ResourceType.WOOD || resource.getResourceType() == ResourceType.STONE)
					.mapToInt(Resource::getAmount).sum();
			effectResolutor.addResources(new Resource(ResourceType.VICTORY, -costSum));
		}
		else {
			effectResolutor.addResources(new Resource(cardType.toResourceType(), -effectResolutor.getResourceAmount(cardType.toResourceType())));
		}
	}

	@Override
	public String toString() {
		if (cardType == CardType.BUILDING) {
			return "At the end of the game, you lose 1 Victory Point for every wood and stone on your Building Cards’ costs";
		}
		else {
			return "At the end of the game, you don’t score Victory Points for any of your " + cardType.toString() + " cards";
		}
	}

}
