package it.polimi.ingsw.LM45.network.server;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class ServerControllerFactory {

	private static ServerController currentServerController;
	private static int instanceCount;
	private static int maxInstanceCount;
	private static long gameStartTimerDelay;
	private static long turnTimerDelay;	
	private static Map<String, ServerController> disconnectedUsersDictionary;
	
	private ServerControllerFactory(){}

	public static void initialize(int maxInstanceCount, long gameStartTimerDelay, long turnTimerDelay) {
		instanceCount = 0;
		ServerControllerFactory.maxInstanceCount = maxInstanceCount;
		ServerControllerFactory.gameStartTimerDelay = gameStartTimerDelay;
		ServerControllerFactory.turnTimerDelay = turnTimerDelay;
		disconnectedUsersDictionary = new HashMap<>();
	}

	public static ServerController getServerControllerInstance(String username){
		if(currentServerController == null || instanceCount >= maxInstanceCount){
			instanceCount = 0;
			currentServerController = createServerControllerInstance(username);
		}
		
		instanceCount++;
		return currentServerController;
	}
	
	public static void addDisconnectedUser(String username, ServerController serverController){
		disconnectedUsersDictionary.put(username, serverController);
	}

	public static void shutdown(){
		// TODO: implement
	}

	private static ServerController createServerControllerInstance(String username) {
		try {
			return new ServerController(maxInstanceCount, gameStartTimerDelay, turnTimerDelay);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
