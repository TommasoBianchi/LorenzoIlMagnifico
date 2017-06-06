package it.polimi.ingsw.LM45.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketFactory implements Runnable {
	
	private ServerSocket serverSocket;
	private ServerControllerFactory serverControllerFactory;
	private boolean isRunning;

	public SocketFactory(ServerControllerFactory serverControllerFactory, int port) throws IOException{
		this.serverControllerFactory = serverControllerFactory;
		this.serverSocket = new ServerSocket(port);
		new Thread(this).start();
		isRunning = true;
	}
	
	public SocketFactory(ServerControllerFactory serverControllerFactory) throws IOException{
		this(serverControllerFactory, 0);
	}
	
	public int getPort(){
		return serverSocket.getLocalPort();
	}

	@Override
	public void run() {
		while(isRunning){
			try {
				Socket socket = serverSocket.accept();
				new SocketServer(socket, serverControllerFactory.getServerControllerInstance());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
