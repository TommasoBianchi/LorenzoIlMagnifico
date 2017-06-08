package it.polimi.ingsw.LM45.network.client;

public class ClientController {
	
	private String username;

	public void setUsername(String username) {
		this.username = username;
	}

	public void notifyPlayerTurn(String player) {
		if(this.username.equals(player)){
			// TODO: play turn
			System.out.println("It's my turn");
		}
		else {
			System.out.println("It's" + player + "turn");
		}
	}
	
}
