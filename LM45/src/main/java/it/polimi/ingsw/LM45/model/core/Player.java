package it.polimi.ingsw.LM45.model.core;

import java.util.List;

import javafx.scene.paint.Color;

import it.polimi.ingsw.LM45.model.cards.LeaderCard;

public class Player {

		private String nickname;
		private Color color;
		private List<LeaderCard> leaderCards;
		private PersonalBoard personalBoard;
		private List<Familiar> familiars;
		// TODO: excommunicationMaluses ??
		private PersonalBonusTile personalBonusTile;
		private boolean payIfTowerIsOccupied;
		private List<Resource> churchSupportBonuses;
		private boolean hasToSkipFirstTurn;
		
}
