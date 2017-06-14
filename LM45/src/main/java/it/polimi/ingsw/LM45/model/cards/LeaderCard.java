package it.polimi.ingsw.LM45.model.cards;

import java.util.Arrays;

import it.polimi.ingsw.LM45.exceptions.IllegalActionException;
import it.polimi.ingsw.LM45.model.core.Player;
import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.effects.CardEffect;
import it.polimi.ingsw.LM45.model.effects.EffectResolutor;

public class LeaderCard {

	private String name;
	private CardEffect effect;
	private Resource[] requisites;

	private transient boolean hasBeenPlayed = false;
	private transient boolean hasBeenActivated = false;

	public LeaderCard(String name, CardEffect effect, Resource[] requisites) {
		this.name = name;
		this.effect = effect;
		this.requisites = requisites;
	}

	public String getName() {
		return this.name;
	}

	public boolean canPlay(Player player) {
		return Arrays.stream(requisites).allMatch(resource -> player.hasResources(resource));
	}

	public void play() {
		hasBeenPlayed = true;
	}

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

	@Override
	public String toString() {
		return name + " (LeaderCard)\n" + "Requisites: "
				+ Arrays.stream(requisites).map(resource -> resource.toString()).reduce("", (a, b) -> a + " " + b) + "\n" + "Effect: "
				+ effect.toString();
	}

}
