package it.polimi.ingsw.LM45.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketFactory implements Runnable {
	
	private ServerSocket serverSocket;
	private ServerController serverController;
	private boolean isRunning;

	public SocketFactory(ServerController serverController, int port) throws IOException{
		this.serverController = serverController;
		this.serverSocket = new ServerSocket(port);
		new Thread(this).start();
		isRunning = true;
	}
	
	public SocketFactory(ServerController serverController) throws IOException{
		this(serverController, 0);
	}
	
	public int getPort(){
		return serverSocket.getLocalPort();
	}

	@Override
	public void run() {
		while(isRunning){
			try {
				Socket socket = serverSocket.accept();
				new SocketServer(socket, serverController);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
