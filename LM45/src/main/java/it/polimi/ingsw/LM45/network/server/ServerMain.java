package it.polimi.ingsw.LM45.network.server;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class ServerMain {
	
	public static void main(String[] args){
		ServerController serverController = null;
		try {
			serverController = new ServerController();
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		try {
			SocketFactory socketFactory = new SocketFactory(serverController, 7000);
			System.out.println("SocketFactory listening on port " + socketFactory.getPort());
			RMIFactory rmiFactory = new RMIFactory(serverController);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
