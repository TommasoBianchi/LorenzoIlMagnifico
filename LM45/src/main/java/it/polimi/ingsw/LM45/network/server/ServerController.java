package it.polimi.ingsw.LM45.network.server;

import java.util.HashMap;
import java.util.Map;

import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.network.client.ClientInterface;

public class ServerController {
	
	Map<String, ClientInterface> users = new HashMap<String, ClientInterface>();

	public void login(String username, ClientInterface clientInterface){
		System.out.println(username + " logged in");
		users.put(username, clientInterface);
	}
	
	public void removeUser(String username){
		users.remove(username);
	}
	
	public void placeFamiliar(String player, FamiliarColor familiarColor, Integer slotID) {
		// TODO: implement
		System.out.println(player +  " placed familiar " + familiarColor + " in slot " + slotID);
	}

	public void increaseFamiliarValue(String player, FamiliarColor familiarColor) {
		// TODO: implement
		System.out.println(player +  " increased value of familiar " + familiarColor);		
	}

	public void playLeaderCard(String player, String leaderCardName) {
		// TODO: implement
		System.out.println(player +  " played leader card " + leaderCardName);
	}

	public void activateLeaderCard(String player, String leaderCardName) {
		// TODO: implement
		System.out.println(player +  " activated leader card " + leaderCardName);
	}

	public void discardLeaderCard(String player, String leaderCardName) {
		// TODO: implement
		System.out.println(player +  " discarded leader card " + leaderCardName);
	}

	public void endTurn(String player) {
		// TODO: implement
		System.out.println(player +  " ended his turn");
	}
	
}
