package it.polimi.ingsw.LM45.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.ArrayList;

public class SocketFactory implements Runnable {
	
	private ServerSocket serverSocket;
	private boolean isRunning;
	private List<SocketServer> createdSockets;

	public SocketFactory(int port) throws IOException{
		this.serverSocket = new ServerSocket(port);
		this.createdSockets = new ArrayList<>();
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
				createdSockets.add(socketServer);
			} catch (IOException e) {
				System.err.println("SocketFactory failed while trying to accept an incoming connection");
				e.printStackTrace();
			}
		}
	}

	public void shutdown(){
		createdSockets.forEach(SocketServer::close);
	}
	
}
