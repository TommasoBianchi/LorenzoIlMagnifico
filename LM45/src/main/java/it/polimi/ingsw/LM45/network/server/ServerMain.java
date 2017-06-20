package it.polimi.ingsw.LM45.network.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import it.polimi.ingsw.LM45.config.ServerConfiguration;
import it.polimi.ingsw.LM45.serialization.FileManager;

public class ServerMain {
	
	public static final Logger LOGGER = Logger.getLogger("Server");
	
	private static SocketFactory socketFactory;
	private static RMIFactory rmiFactory;

	public static void main(String[] args) {			
		ServerConfiguration serverConfiguration = new ServerConfiguration(4, 30000, 60000, 7000); // Defaults
		try {
			serverConfiguration = FileManager.loadConfiguration(ServerConfiguration.class);
		}
		catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			LOGGER.log(Level.WARNING, "Couldn't load server configuration. Using the default one.", e);
		}

		ServerControllerFactory.initialize(serverConfiguration.getMaxPlayersAmount(),
				serverConfiguration.getGameStartTimerDelay(), serverConfiguration.getTurnTimerDelay());

		try {
			socketFactory = new SocketFactory(serverConfiguration.getServerSocketPort());
			LOGGER.log(Level.CONFIG, "SocketFactory listening on port " + socketFactory.getPort());
		}
		catch (IOException e) {
			LOGGER.log(Level.WARNING, "Couldn't start sockets' infrastructure. Please connect using RMI.", e);
		}

		try {
			rmiFactory = new RMIFactory();
		}
		catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Couldn't start RMI's infrastructure. Please connect using sockets.", e);
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
