package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.CardType;
import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;
import it.polimi.ingsw.LM45.model.core.SlotType;

/**
 * This interface provides all effects an easy but controlled access to the {@link it.polimi.ingsw.LM45.model.core.Player} and to some of its method, while also providing access to some methods that
 * requires client-side interaction such as doBonusAction or chooseFrom.
 * 
 * @author Tommy
 *
 */
public interface EffectResolutor {

	/**
	 * @param resource
	 *            the resource to add to the player
	 */
	public void addResources(Resource resource);

	/**
	 * @param resourceType
	 *            the resourceType to inspect
	 * @return the amount of resources of the given type that the player has
	 */
	public int getResourceAmount(ResourceType resourceType);

	/**
	 * @param resource
	 *            the resource to check
	 * @return true if the player has the given resources
	 */
	public boolean hasResources(Resource resource);

	/**
	 * @param resource
	 *            the bonus resources gained when supporting the church
	 */
	public void addChurchSupportBonus(Resource resource);

	/**
	 * @param color
	 *            the familiarColor of the player's familiar to add the bonus to
	 * @param bonus
	 *            the bonus to add
	 */
	public void addFamiliarBonus(FamiliarColor color, int bonus);

	/**
	 * @param color
	 *            the familiarColor of the player's familiar which value we want to set
	 * @param bonus
	 *            the new value to set
	 */
	public void setFamiliarValue(FamiliarColor color, int bonus);

	/**
	 * @param servantBonusCostModifier
	 *            the new cost (in SERVANTS) to increase by one the value of one of the player's familiars
	 */
	public void modifyServantCost(int servantBonusCostModifier);

	/**
	 * Sets that the player has to skip the first round of each turn
	 */
	public void setHasToSkipFirstRound();

	/**
	 * Clear the requisites to pick up TERRITORY cards for this player
	 */
	public void noTerritoryRequisites();

	/**
	 * @param value
	 *            true if this player has to pay to enter an occupied tower, false otherwise
	 */
	public void setPayIfTowerIsOccupied(boolean value);

	/**
	 * @param permanentEffect
	 *            the cardEffect we want to add to this player's permanent effects
	 */
	public void addPermanentEffect(CardEffect permanentEffect);

	/**
	 * @param card
	 *            the card we want to know if the player can pick
	 * @return true if the player can add the card to its personalBoard
	 */
	public boolean canAddCard(Card card);

	/**
	 * @param card
	 *            the card to add to the player's personalBoard
	 */
	public void addCard(Card card);

	/**
	 * @param value
	 *            the value of the familiar placed on the HARVEST slot
	 */
	public void harvest(int value);

	/**
	 * @param value
	 *            the value of the familiar placed on the PRODUCTION slot
	 */
	public void produce(int value);

	/**
	 * @param cardType
	 *            the cardType of the cards of which we want to get the total cost
	 * @return an array containing all the resources representing all the costs of all the cards of the given cardType
	 */
	public Resource[] getCardsTotalCost(CardType cardType);

	/**
	 * @param slotType
	 *            the slots on which we can do a bonus action
	 * @param diceNumber
	 *            the value of the action
	 * @param discount
	 *            the resource discount on this bonus action
	 */
	public void doBonusAction(SlotType slotType, int diceNumber, Resource[] discount);

	/**
	 * @return a cardEffect copied from all the cardEffects of all the leaderCards played by other players
	 */
	public CardEffect copyEffect();

	/**
	 * @param alternatives
	 *            the alternative to choose from
	 * @return an element in the alternatives array choosen by the user
	 */
	public <T> T chooseFrom(T[] alternatives);

}
