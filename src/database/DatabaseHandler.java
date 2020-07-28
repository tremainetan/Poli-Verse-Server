package database;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import datastructures.Data;
import datastructures.MessagePacketDB;
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
	
	private MessagePacketDB readSocketIn() throws Exception {
		
		MessagePacketDB obj = null;
		obj = (MessagePacketDB) socketIn.readObject();
		return obj;
		
	}
	
	public void run() {
		////READ MESSAGES INCOMING
		try {
			while (true) {
				MessagePacketDB messageIncoming = null;
				messageIncoming = readSocketIn();
				if (messageIncoming.MESSAGESTRING != null || messageIncoming.CONNECTED) {
					//Message String is not null
					//Client is still connected
					if (messageIncoming.MESSAGESTRING.equals("EXISTS")) {
						String name = messageIncoming.FROM;
						String password = messageIncoming.TO;
						MessagePacketDB messageSending = null;
						
						if (!FileManager.nameExists(name)) {
							FileManager.addName(name, password);
							messageSending = new MessagePacketDB(null, name, "false", null, true);
						}
						else messageSending = new MessagePacketDB(null, name, "true", null, true);
						socketOut.writeObject(messageSending);
					}
					else if (messageIncoming.MESSAGESTRING.equals("CHECK")) {
						//Getting Password
						String name = messageIncoming.FROM;
						Data data = FileManager.data.get(name);
						MessagePacketDB messageSending = new MessagePacketDB(null, name, null, data, true);
						socketOut.writeObject(messageSending);
					}
				}
				else break;
			}
		}
		catch (Exception e) {}
		try {
			socket.close();
		} catch (Exception e) {e.printStackTrace();}
	}
	
}
