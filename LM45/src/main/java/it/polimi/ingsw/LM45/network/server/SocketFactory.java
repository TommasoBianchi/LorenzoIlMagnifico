package it.polimi.ingsw.LM45.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketFactory implements Runnable {
	
	private ServerSocket serverSocket;
	private boolean isRunning;

	public SocketFactory(int port) throws IOException{
		this.serverSocket = new ServerSocket(port);
		new Thread(this).start();
		isRunning = true;
	}
	
	public SocketFactory() throws IOException{
		this(0);
	}
	
	public int getPort(){
		return serverSocket.getLocalPort();
	}

	@Override
	public void run() {
		while(isRunning){
			try {
				Socket socket = serverSocket.accept();
				SocketServer socketServer = new SocketServer(socket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void shutdown(){
		// TODO: implement
	}
	
}
