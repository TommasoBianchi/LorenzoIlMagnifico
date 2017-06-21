package it.polimi.ingsw.LM45.model.core;

import it.polimi.ingsw.LM45.exceptions.IllegalActionException;

public class Familiar {

	private Player player;
	private FamiliarColor familiarColor;
	private int value;
	private int servantBonus;
	private int otherBonuses;
	private boolean valueIsStatic;
	private int servantBonusCost;
	private boolean isPlaced;

	/**
	 * @param player
	 *            the player owning this familiar
	 * @param familiarColor
	 *            the color of this familiar (WHITE, ORANGE, BLACK or UNCOLORED)
	 */
	public Familiar(Player player, FamiliarColor familiarColor) {
		this.player = player;
		this.familiarColor = familiarColor;
		this.value = 0;
		this.servantBonus = 0;
		this.otherBonuses = 0;
		this.valueIsStatic = false;
		this.servantBonusCost = 1;
		this.isPlaced = false;
	}

	/**
	 * clears any bonus given to this familiar by spending servants
	 */
	public void clearServantsBonus() {
		servantBonus = 0;
	}

	/**
	 * @return the value of this familiar (including bonuses)
	 */
	public int getValue() {
		return value + servantBonus + otherBonuses;
	}

	/**
	 * @param value
	 *            the new value of this familiar
	 * @param valueIsStatic
	 *            set this to true to lock the value of this familiar between different turns
	 */
	public void setValue(int value, boolean valueIsStatic) {
		if (!this.valueIsStatic) {
			this.value = value;
			this.valueIsStatic = valueIsStatic;
		}
	}

	/**
	 * @param value
	 *            the new value of this familiar
	 */
	public void setValue(int value) {
		setValue(value, false);
	}

	/**
	 * @param bonus
	 *            a new bonus to add to this familiar (not given by servants)
	 */
	public void addBonus(int bonus) {
		this.otherBonuses += bonus;
	}

	/**
	 * @return the player owning this familiar
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * @return the color of this familiar (WHITE, ORANGE, BLACK or UNCOLORED)
	 */
	public FamiliarColor getFamiliarColor() {
		return this.familiarColor;
	}

	/**
	 * Spend a servant (or more if servantBonusCost has been modified) to increase by one the value of this familiar
	 * @throws IllegalActionException if the player has not enough resources to pay for the increment
	 */
	public void addServantsBonus() throws IllegalActionException {
		if (this.player.hasResources(new Resource(ResourceType.SERVANTS, servantBonusCost))) {
			servantBonus++;
			this.player.addResources(new Resource(ResourceType.SERVANTS, -servantBonusCost));
		}
		else
			throw new IllegalActionException("Cannot increase value of familiar " + familiarColor + ": not enough resources!");
	}

	/**
	 * @return whether or not this familiar has been placed in a slot
	 */
	public boolean getIsPlaced() {
		return this.isPlaced;
	}

	/**
	 * @param value
	 *            whether or not this familiar has been placed in a slot
	 */
	public void setIsPlaced(boolean value) {
		this.isPlaced = value;
	}

	/**
	 * @param cost
	 *            the cost (in servants) of increasing by one the value of this familiar
	 */
	public void setServantBonusCost(int cost) {
		this.servantBonusCost = cost;
	}

}
