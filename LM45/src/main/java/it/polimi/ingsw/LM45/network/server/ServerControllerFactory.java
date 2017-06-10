package it.polimi.ingsw.LM45.network.server;

import java.io.FileNotFoundException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class ServerControllerFactory {

	private ServerController currentServerController;
	private int instanceCount;
	private int maxInstanceCount;
	private long gameStartTimerDelay;
	private long turnTimerDelay;

	public ServerControllerFactory(int maxInstanceCount, long gameStartTimerDelay, long turnTimerDelay) {
		this.instanceCount = 0;
		this.maxInstanceCount = maxInstanceCount;
		this.gameStartTimerDelay = gameStartTimerDelay;
		this.turnTimerDelay = turnTimerDelay;
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
			return new ServerController(maxInstanceCount, gameStartTimerDelay, turnTimerDelay);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
