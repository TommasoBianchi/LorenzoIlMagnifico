package it.polimi.ingsw.LM45.network.server;

import java.io.FileNotFoundException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class ServerControllerFactory {

	private ServerController currentServerController;
	private int instanceCount;
	private int maxInstanceCount;
	
	public ServerControllerFactory(int maxInstanceCount){
		this.currentServerController = createServerControllerInstance();
		this.instanceCount = 0;
		this.maxInstanceCount = maxInstanceCount;
	}
	
	public ServerController getServerControllerInstance(){
		if(instanceCount >= maxInstanceCount){
			instanceCount = 0;
			currentServerController = createServerControllerInstance();
		}
		
		instanceCount++;
		return currentServerController;
	}
	
	private ServerController createServerControllerInstance(){
		try {
			return new ServerController();
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
