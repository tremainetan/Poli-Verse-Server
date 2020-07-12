package server;

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
	
	public ClientHandler(Socket socket) {
		this.socket = socket;
		this.lastDate = null;
	}
	
	public void run() {
		
		String username = "";
		try {
			socketIn = new ObjectInputStream(socket.getInputStream());
			socketOut = new ObjectOutputStream(socket.getOutputStream());
			socketOut.flush();
			
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
				synchronized (Main.writers) {
					if (!username.isBlank() && !Main.writers.containsKey(username)) {
						Main.writers.put(username, socketOut);
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
				Main.writers.remove(username);
			}
			try {
				socket.close();
			} catch (Exception e) {e.printStackTrace();}
		}
	}
	
}
