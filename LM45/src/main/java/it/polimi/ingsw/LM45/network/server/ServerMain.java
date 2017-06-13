package it.polimi.ingsw.LM45.network.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import it.polimi.ingsw.LM45.config.ServerConfiguration;
import it.polimi.ingsw.LM45.serialization.FileManager;

public class ServerMain {

	public static void main(String[] args) {
		ServerConfiguration serverConfiguration = new ServerConfiguration(4, 30000, 60000, 7000); // Defaults
		try {
			serverConfiguration = FileManager.loadConfiguration(ServerConfiguration.class);
		}
		catch (JsonSyntaxException | JsonIOException | FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ServerControllerFactory serverControllerFactory = new ServerControllerFactory(serverConfiguration.getMaxPlayersAmount(),
				serverConfiguration.getGameStartTimerDelay(), serverConfiguration.getTurnTimerDelay());

		try {
			SocketFactory socketFactory = new SocketFactory(serverControllerFactory, serverConfiguration.getServerSocketPort());
			System.out.println("SocketFactory listening on port " + socketFactory.getPort());
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			RMIFactory rmiFactory = new RMIFactory(serverControllerFactory);
		}
		catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
