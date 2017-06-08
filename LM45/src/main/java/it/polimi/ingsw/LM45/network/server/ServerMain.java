package it.polimi.ingsw.LM45.network.server;

import java.io.IOException;

public class ServerMain {
	
	public static void main(String[] args){
		ServerControllerFactory serverControllerFactory = new ServerControllerFactory(4, 25000);
		
		try {
			SocketFactory socketFactory = new SocketFactory(serverControllerFactory, 7000);
			System.out.println("SocketFactory listening on port " + socketFactory.getPort());
			RMIFactory rmiFactory = new RMIFactory(serverControllerFactory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
