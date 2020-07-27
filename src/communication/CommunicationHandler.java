package communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import datastructures.MessagePacket;
import main.ServerMain;

public class CommunicationHandler implements Runnable {
	
	private Socket socket;
	private ObjectInputStream socketIn;
	private ObjectOutputStream socketOut;
	
	private String username;
	
	public CommunicationHandler(Socket socket) {
		
		this.socket = socket;
		
		try {
			
			socketIn = new ObjectInputStream(socket.getInputStream());
			socketOut = new ObjectOutputStream(socket.getOutputStream());
			socketOut.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private MessagePacket readSocketIn() throws Exception {
		
		MessagePacket obj = null;
		obj = (MessagePacket) socketIn.readObject();
		return obj;
		
	}
	
	private void getName() {
		
		MessagePacket messageReceive = null;
		try {
			messageReceive = readSocketIn();
		} catch (Exception e) {}
		if (messageReceive.TO == null) {
			//Message is to Server
			if (messageReceive.CONNECTED) {
				if (messageReceive.MESSAGESTRING.equals("SUBMITNAME")) {
					
					username = messageReceive.FROM;
				}
			}
		}
		synchronized (ServerMain.sockets) {
			ServerMain.sockets.put(username, socket);
		}
	}
	
	public void run() {
		
		getName();
		System.out.println(username + " has joined");
		
		while (true) {
			////READ MESSAGES INCOMING
			MessagePacket messageIncoming = null;
			try {
				messageIncoming = readSocketIn();
			} catch (Exception e) {}
			
			if (messageIncoming.CONNECTED) {
				System.out.println("Received a message from: " + messageIncoming.FROM);
			}
			else break;
		}
		
		if (username != null && socket != null) {
			//Client Quitting
			System.out.println(username + " has left");
			ServerMain.sockets.remove(username);
		}
		try {
			socket.close();
		} catch (Exception e) {e.printStackTrace();}
	}
	
}
