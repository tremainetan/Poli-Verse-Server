package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;

import datastructures.MessagePacket;

public class ClientHandler implements Runnable {
	
	private Socket socket;
	private ObjectInputStream socketIn;
	private ObjectOutputStream socketOut;
	
	private LocalDate lastDate;
	
	private String username;
	
	public ClientHandler(Socket socket) {
		
		this.socket = socket;
		this.lastDate = null;
		
		try {
			
			socketIn = new ObjectInputStream(socket.getInputStream());
			socketOut = new ObjectOutputStream(socket.getOutputStream());
			socketOut.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void getName() {
		try {
			Object obj = null;
			boolean hasObject = true;
			while (hasObject) {
				while (true) {
					try {
						obj = socketIn.readObject();
						hasObject = true;
						break;
					}
					catch (Exception e) {hasObject = false;}
				}
				
				//Unpack and Process Object
				MessagePacket messageReceive = (MessagePacket) obj;
	
				if (messageReceive.TO == null) {
					//Message is to Server
					if (messageReceive.MESSAGESTRING.equals("SUBMITNAME")) {
						username = messageReceive.FROM;
						System.out.println(username);
					}
				}
				if (username == null) {
					return;
				}
				synchronized (Main.sockets) {
					if (!username.isBlank() && !Main.sockets.containsKey(username)) {
						Main.sockets.put(username, socket);
						break;
					}
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		
		////GET NAME
		getName();
		
		try {
			Object obj = null;
			boolean hasObject = true;
			while (hasObject) {
				while (true) {
					try {
						obj = socketIn.readObject();
						hasObject = true;
						break;
					}
					catch (Exception e) {hasObject = false;}
				}
				
				//Unpack and Process Object
				MessagePacket messageReceive = (MessagePacket) obj;
	
				if (messageReceive.TO == null) {
					//Message is to Server
					if (messageReceive.MESSAGESTRING.equals("SUBMITNAME")) {
						username = messageReceive.FROM;
					}
				}
				if (username == null) {
					return;
				}
				synchronized (Main.sockets) {
					if (!Main.sockets.containsKey(username)) {
						Main.sockets.put(username, socket);
						break;
					}
				}
				
			}
			
		} catch (Exception e) {e.printStackTrace();}
		
		try {
			MessagePacket data = new MessagePacket();
			data.FROM = "";
			data.TO = username;
			data.MESSAGESTRING = "NAMEACCEPTED";
			data.MESSAGEFILE = null;
			socketOut.writeObject(data);
			
			//Date Printing
			LocalDate date = LocalDate.now();
			if (date != lastDate) {
				System.out.println("-----" + date + "-----");
				lastDate = date;
			}
			System.out.println(username + " has joined");
			
			////READ MESSAGES INCOMING
			while (socketIn.readObject() != null) {
				data = (MessagePacket) socketIn.readObject();
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (username != null && socketOut != null) {
				//Client Quitting
				System.out.println(username + " has left");
				Main.sockets.remove(username);
			}
			try {
				socket.close();
			} catch (Exception e) {e.printStackTrace();}
		}
	}
	
}
