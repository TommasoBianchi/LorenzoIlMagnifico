package it.polimi.ingsw.LM45.network.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import it.polimi.ingsw.LM45.config.ServerConfiguration;
import it.polimi.ingsw.LM45.serialization.FileManager;

public class ServerMain {
	
	private static SocketFactory socketFactory;
	private static RMIFactory rmiFactory;
	private static ServerControllerFactory serverControllerFactory;

	public static void main(String[] args) {
		ServerConfiguration serverConfiguration = new ServerConfiguration(4, 30000, 60000, 7000); // Defaults
		try {
			serverConfiguration = FileManager.loadConfiguration(ServerConfiguration.class);
		}
		catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Couldn't load server configuration. Using the default one.");
			e.printStackTrace();
		}

		ServerControllerFactory.initialize(serverConfiguration.getMaxPlayersAmount(),
				serverConfiguration.getGameStartTimerDelay(), serverConfiguration.getTurnTimerDelay());

		try {
			socketFactory = new SocketFactory(serverControllerFactory, serverConfiguration.getServerSocketPort());
			System.out.println("SocketFactory listening on port " + socketFactory.getPort());
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Couldn't start sockets' infrastructure. Please connect using RMI.");
			e.printStackTrace();
		}

		try {
			rmiFactory = new RMIFactory(serverControllerFactory);
		}
		catch (RemoteException e) {
			// TODO Auto-generated catch block
			System.out.println("Couldn't start RMI's infrastructure. Please connect using sockets.");
			e.printStackTrace();
		}
		
		Runtime.getRuntime().addShutdownHook(new Thread() {			
			@Override
			public void run() {
				socketFactory.shutdown();
				rmiFactory.shutdown();
				ServerControllerFactory.shutdown();
			}
		});
	}

}
