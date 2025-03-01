package communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

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
			ServerMain.sockets.put(username, socketOut);
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
				String FROM = messageIncoming.FROM;
				ArrayList<String> TO = messageIncoming.TO;
				if (FROM != null && TO != null) {
					MessagePacket messageSending = new MessagePacket(FROM, TO, messageIncoming.MESSAGESTRING, true);
					Collections.sort(TO);
					ArrayList<ObjectOutputStream> toSockets = new ArrayList<ObjectOutputStream>();
					for (int i = 0; i < TO.size(); i++) {
						String nameTo = TO.get(i);
						toSockets.add(ServerMain.sockets.get(nameTo));
					}
					for (int i = 0; i < toSockets.size(); i++) {
						ObjectOutputStream toSocket = toSockets.get(i);
						if (toSocket != null) {
							try {
								toSocket.writeObject(messageSending);
							} catch (IOException e) {
								e.printStackTrace();
							}	
						}
					}
					
				}
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
