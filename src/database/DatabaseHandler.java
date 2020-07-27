package database;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import datastructures.MessagePacket;
import framework.FileManager;

public class DatabaseHandler implements Runnable {
	
	private Socket socket;
	private ObjectInputStream socketIn;
	private ObjectOutputStream socketOut;
	
	public DatabaseHandler(Socket socket) {
		
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
	
	public void run() {
		////READ MESSAGES INCOMING
		while (true) {
			MessagePacket messageIncoming = null;
			try {
				messageIncoming = readSocketIn();
			} catch (Exception e) {}
			if (messageIncoming.MESSAGESTRING != null || messageIncoming.CONNECTED) {
				//Message String is not null
				//Client is still connected
				if (messageIncoming.MESSAGESTRING.equals("CHECK")) {
					String name = messageIncoming.FROM;
					MessagePacket messageSending = null;
					
					if (!FileManager.nameExists(name)) {
						FileManager.addName(name);
						messageSending = new MessagePacket(null, name, "false", null, true);
					}
					else messageSending = new MessagePacket(null, name, "true", null, true);
					try {
						socketOut.writeObject(messageSending);
					}
					catch (Exception e) {e.printStackTrace();}
				}
			}
			else break;
		}
		try {
			socket.close();
		} catch (Exception e) {e.printStackTrace();}
	}
	
}
