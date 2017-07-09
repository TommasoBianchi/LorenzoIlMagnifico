package it.polimi.ingsw.LM45.network.server;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class ServerControllerFactory {

	private static ServerController currentServerController;
	private static int currentGameID = -1;
	private static int instanceCount;
	private static int maxInstanceCount;
	private static long gameStartTimerDelay;
	private static long turnTimerDelay;
	private static Map<String, ServerController> disconnectedUsersDictionary;
	private static Set<ServerController> runningServerControllerInstances;

	private ServerControllerFactory() {
	}

	public static void initialize(int maxInstanceCount, long gameStartTimerDelay, long turnTimerDelay) {
		instanceCount = 0;
		ServerControllerFactory.maxInstanceCount = maxInstanceCount;
		ServerControllerFactory.gameStartTimerDelay = gameStartTimerDelay;
		ServerControllerFactory.turnTimerDelay = turnTimerDelay;
		disconnectedUsersDictionary = new HashMap<>();
		runningServerControllerInstances  = new HashSet<>();
	}

	public static ServerController getServerControllerInstance(String username) {
		if (disconnectedUsersDictionary.containsKey(username)) {
			ServerController serverController = disconnectedUsersDictionary.remove(username);
			if(runningServerControllerInstances.contains(serverController))
				return serverController;
			else
				disconnectedUsersDictionary.remove(username);
		}

		if (currentServerController == null || instanceCount >= maxInstanceCount) {
			instanceCount = 0;
			currentServerController = createServerControllerInstance();
		}

		instanceCount++;
		return currentServerController;
	}

	public static void addDisconnectedUser(String username, ServerController serverController) {
		disconnectedUsersDictionary.put(username, serverController);
	}
	
	public static void removeRunningServerController(ServerController serverController){
		runningServerControllerInstances.remove(serverController);
	}

	public static void shutdown() {
		for(ServerController serverController : runningServerControllerInstances)
			serverController.shutdown();
	}

	private static ServerController createServerControllerInstance() {
		try {
			ServerController serverController = new ServerController(++currentGameID, maxInstanceCount, gameStartTimerDelay, turnTimerDelay);
			runningServerControllerInstances.add(serverController);
			return serverController;
		}
		catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			System.err.println("ServerSocketFactory unable to instantiate a ServerController -- returning null (probably something will break)");
			e.printStackTrace();
			return null;
		}
	}

}
