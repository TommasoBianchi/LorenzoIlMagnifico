package it.polimi.ingsw.LM45.model.cards;

import java.io.Serializable;
import java.util.Arrays;

import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class LeaderCard implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String name;
	private CardEffect effect;
	private Resource[] requisites;

	private transient boolean hasBeenPlayed = false;
	private transient boolean hasBeenActivated = false;

	/**
	 * @param name name of the Card
	 * @param effect the effect that is activated when player activates the leaderCard
	 * @param requisites the resources player needs to have to play the card
	 */
	public LeaderCard(String name, CardEffect effect, Resource[] requisites) {
		this.name = name;
		this.effect = effect;
		this.requisites = requisites;
	}

	/**
	 * @return the name of the leaderCard
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param player player that what to play the leaderCard
	 * @return true if player can play the leaderCard
	 */
	public boolean canPlay(Player player) {
		return Arrays.stream(requisites).allMatch(resource -> player.hasResources(resource));
	}

	/**
	 * Method called by player to play the leaderCard
	 * @throws IllegalActionException exception that means that leaderCard it's been already activated
	 */
	public void play(EffectResolutor effectResolutor) throws IllegalActionException {
		hasBeenPlayed = true;
		if(effect.getEffectsArePermanent())
			activate(effectResolutor);
	}

	/**
	 * @param effectResolutor interface with all methods that model can use to call the EffectController
	 * @throws IllegalActionException exception that means that leaderCard it's been already activated
	 * or it's not been played yet
	 */
	public void activate(EffectResolutor effectResolutor) throws IllegalActionException {
		if (hasBeenPlayed && !hasBeenActivated) {
			hasBeenActivated = false;
			effect.resolveEffects(effectResolutor);
		}
		else if (hasBeenActivated) {
			throw new IllegalActionException("LeaderCard " + name + " has already been activated in this turn");
		}
		else {
			throw new IllegalActionException("LeaderCard " + name + " has not been played yet");
		}
	}
	
	/**
	 * @return whether or not this leaderCard has been played by its owner
	 */
	public boolean getHasBeenPlayed(){
		return this.hasBeenPlayed;
	}
	
	/**
	 * @return the effect of this leaderCard
	 */
	public CardEffect getEffect(){
		return this.effect;
	}

	@Override
	public String toString() {
		return name + " (LeaderCard)\n" + "Requisites: "
				+ Arrays.stream(requisites).map(Resource::toString).reduce("", (a, b) -> a + " " + b) + "\n" + "Effect: "
				+ effect.toString();
	}

}
