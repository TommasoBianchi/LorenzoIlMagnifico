package it.polimi.ingsw.LM45.network.server;

import java.io.IOException;

public class ServerMain {
	
	public static void main(String[] args){
		ServerController serverController = new ServerController();
		
		try {
			SocketFactory socketFactory = new SocketFactory(serverController, 7000);
			System.out.println("SocketFactory listening on port " + socketFactory.getPort());
			RMIFactory rmiFactory = new RMIFactory(serverController);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
