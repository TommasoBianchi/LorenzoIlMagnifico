package testUtilities;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class FakeEffectResolutor implements EffectResolutor {
	
	private Player player;
	
	public FakeEffectResolutor(Player player) {
		this.player = player;
	}

	@Override
	public void addResources(Resource resource) {
		player.addResources(resource);
	}

	@Override
	public int getResourceAmount(ResourceType resourceType) {
		return player.getResourceAmount(resourceType);
	}

	@Override
	public boolean hasResources(Resource resource) {
		return player.hasResources(resource);
	}

	@Override
	public void addChurchSupportBonus(Resource resource) {
		player.addChurchSupportBonus(resource);
	}

	@Override
	public void addFamiliarBonus(FamiliarColor color, int bonus) {
		player.addFamiliarBonus(color, bonus);
	}

	@Override
	public void setFamiliarValue(FamiliarColor color, int bonus) {
		player.setFamiliarValue(color, bonus);
	}

	@Override
	public void modifyServantCost(int servantBonusCostModifier) {
		player.modifyServantCost(servantBonusCostModifier);
	}

	@Override
	public void setHasToSkipFirstRound() {
		player.setHasToSkipFirstRound();
	}

	@Override
	public void noTerritoryRequisites() {
		player.noTerritoryRequisites();
	}

	@Override
	public void setPayIfTowerIsOccupied(boolean value) {
		player.setPayIfTowerIsOccupied(value);
	}

	@Override
	public void addPermanentEffect(CardEffect permanentEffect) {
		player.addPermanentEffect(permanentEffect);
	}

	@Override
	public boolean canAddCard(Card card) {
		return player.canAddCard(card);
	}

	@Override
	public void addCard(Card card) {
		player.addCard(card);
	}

	@Override
	public void harvest(int value) {
		player.harvest(this, value);
	}

	@Override
	public void produce(int value) {
		player.produce(this, value);
	}

	@Override
	public void doBonusAction(SlotType slotType, int diceNumber, Resource[] discount) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CardEffect copyEffect() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T chooseFrom(T[] alternatives) {
		return alternatives[0];
	}

}
