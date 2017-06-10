package it.polimi.ingsw.LM45.network.server;

import java.io.IOException;
import java.rmi.RemoteException;

public class ServerMain {
	
	public static void main(String[] args){
		int maxPlayersAmount = 2;
		int gameStartTimerDelay = 5000; // Time in milliseconds
		int socketPort = 7000;
		// TODO: think of what may be configurable from args
		
		ServerControllerFactory serverControllerFactory = new ServerControllerFactory(maxPlayersAmount, gameStartTimerDelay);
		
		try {
			SocketFactory socketFactory = new SocketFactory(serverControllerFactory, socketPort);
			System.out.println("SocketFactory listening on port " + socketFactory.getPort());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			RMIFactory rmiFactory = new RMIFactory(serverControllerFactory);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
