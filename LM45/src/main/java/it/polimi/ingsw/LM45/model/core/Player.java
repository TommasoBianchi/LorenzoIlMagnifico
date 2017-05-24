package it.polimi.ingsw.LM45.model.core;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import it.polimi.ingsw.LM45.model.cards.Card;
import it.polimi.ingsw.LM45.model.cards.LeaderCard;

public class Player {

		private String nickname;
		private Color color;
		private List<LeaderCard> leaderCards;
		private PersonalBoard personalBoard;
		private Familiar[] familiars;
		// TODO: excommunicationMaluses ??
		private PersonalBonusTile personalBonusTile;
		private boolean payIfTowerIsOccupied;
		private List<Resource> churchSupportBonuses;
		private boolean hasToSkipFirstTurn;
		
		public boolean canAddCard(Card card) {
			return card.canPick(this) && personalBoard.canAddCard(card);
		}
		
		public void addCard(Card card){
			personalBoard.addCard(card);
		}
		
		public void addLeaderCard(LeaderCard leaderCard){
			if(leaderCards == null)
				leaderCards = new ArrayList<LeaderCard>();
			leaderCards.add(leaderCard);
		}
		
		public void playLeaderCard(LeaderCard leaderCard){
			// TODO: implement
		}
		
		public void discardLeaderCard(LeaderCard leaderCard){
			leaderCards.remove(leaderCard);
			// TODO: add a council privilege
		}
		
		public void addResources(Resource resource){
			if(resource.getAmount() > 0)
				personalBoard.addResources(resource);
			else
				personalBoard.removeResources(resource);
		}
		
		public int getResourceAmount(ResourceType resourceType){
			return personalBoard.getResourceAmount(resourceType);
		}
		
		public boolean hasResources(Resource resource){
			return personalBoard.hasResources(resource);
		}
		
		public void addFamiliarBonus(FamiliarColor color, int bonus){
			for(Familiar familiar:familiars ){
				if (familiar.getFamiliarColor()==color)
					familiar.addBonus(bonus);
			}
		}
		
		public void setFamiliarValue(FamiliarColor color, int value){
			for(Familiar familiar:familiars ){
				if (familiar.getFamiliarColor()==color)
					familiar.setValue(value);
			}
		}
		
		public void modifyServantCost(int cost){
			for(Familiar familiar:familiars ){
				familiar.setServantBonusCost(cost);
			}
		}
		
		public void noTerritoryRequisites() {
			personalBoard.clearTerritoryRequisites();
		}
		
		public void addChurchSupportBonus(Resource resource) {
			churchSupportBonuses.add(resource);
		}
		
		public void setHasToSkipFirstTurn() {
			hasToSkipFirstTurn = true;
		}
		
		/*public activateLeaderCard(leaderCard: LeaderCard) : void		
		public addExcommunication(ex: Excommunication) : void*/
}
