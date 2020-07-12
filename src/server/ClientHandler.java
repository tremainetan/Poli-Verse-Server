package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.HashMap;

import datastructures.MessagePacket;

public class ClientHandler implements Runnable {
	
	private Socket socket;
	private ObjectInputStream socketIn;
	private ObjectOutputStream socketOut;
	
	private LocalDate lastDate;
	
	private static HashMap<String, OutputStream> writers = new HashMap<String, OutputStream>();
	
	public ClientHandler(Socket socket) {
		this.socket = socket;
		this.lastDate = null;
	}
	
	public void run() {
		
		String username = "";
		try {
			socketIn = new ObjectInputStream(socket.getInputStream());
			socketOut = new ObjectOutputStream(socket.getOutputStream());
			
			//Getting Name
			while (true) {
				
				//Create New File and Write to It
				MessagePacket data = new MessagePacket();
				data.FROM = "";
				data.TO = "";
				data.MESSAGESTRING = "SUBMITNAME";
				data.MESSAGEFILE = null;
				socketOut.writeObject(data);
		        
		        //Get name from Client
				data = (MessagePacket) socketIn.readObject();
				username = data.FROM;
				if (username == null) {
					return;
				}
				synchronized (writers) {
					if (!username.isBlank() && !writers.containsKey(username)) {
						writers.put(username, socketOut);
						break;
					}
				}
			}
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
			
			//Reading From Client
			while (socketIn.readObject() != null) {
				data = (MessagePacket) socketIn.readObject();
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (username != null && socketOut != null) {
				//Client Quitting
				System.out.println(username + " has left");
				writers.remove(username);
			}
			try {
				socket.close();
			} catch (Exception e) {e.printStackTrace();}
		}
	}
	
}
