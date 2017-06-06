package it.polimi.ingsw.LM45.network.server;

import java.io.FileNotFoundException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class ServerControllerFactory {

	private ServerController currentServerController;
	private int instanceCount;
	private int maxInstanceCount;
	private long gameStartTimerDelay;

	public ServerControllerFactory(int maxInstanceCount, long gameStartTimerDelay) {
		this.instanceCount = 0;
		this.maxInstanceCount = maxInstanceCount;
		this.gameStartTimerDelay = gameStartTimerDelay;
		this.currentServerController = createServerControllerInstance();
	}

	public ServerController getServerControllerInstance(){
		if(instanceCount >= maxInstanceCount){
			instanceCount = 0;
			currentServerController = createServerControllerInstance();
		}
		
		instanceCount++;
		return currentServerController;
	}

	private ServerController createServerControllerInstance() {
		try {
			return new ServerController(gameStartTimerDelay);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
