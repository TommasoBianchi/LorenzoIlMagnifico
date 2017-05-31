package it.polimi.ingsw.LM45.network.client;

public class ClientController {

	public void handleNewUser(String username){
		System.out.println("New user connected: "+ username);
	}

	public void handleNewMessage(String username, String message){
		System.out.println(username + " say: "+ message);
	}
	
}
