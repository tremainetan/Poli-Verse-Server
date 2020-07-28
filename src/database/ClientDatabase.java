package database;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import datastructures.Data;
import datastructures.MessagePacketDB;

public class ClientDatabase implements Runnable {
	
	private Socket socket;
	private ObjectInputStream socketIn;
	private ObjectOutputStream socketOut;
	
	public ClientDatabase(String serverAddress, int PORT) {
		
		createConnection(serverAddress, PORT);
		
	}
	
	private MessagePacketDB readSocketIn() throws Exception {
		
		MessagePacketDB obj = null;
		obj = (MessagePacketDB) socketIn.readObject();
		return obj;
		
	}
	
	public void run() {
		
		MessagePacketDB messageReceive = null;
		try {
			messageReceive = readSocketIn();
		} catch (Exception e) {}
		//Temporary Processing:
		if (messageReceive != null) {
			System.out.println("Received message from " + messageReceive.FROM + ": " + messageReceive.MESSAGESTRING);
		}
		
	}
	
	public void disconnect() throws Exception {
		MessagePacketDB messageSending = new MessagePacketDB(null, null, null, null, false);
		socketOut.writeObject(messageSending);
	}
	
	public boolean nameExists(String name, String password) throws Exception {
		boolean nameExists = true;
		
		MessagePacketDB messageSending = new MessagePacketDB(name, password, "EXISTS", null, true);
		socketOut.writeObject(messageSending);
		
		MessagePacketDB messageReceive = readSocketIn();
		if (messageReceive.FROM == null && messageReceive.TO.equals(name)) {
			if (messageReceive.MESSAGESTRING.equals("true")) nameExists = true;
			else nameExists = false;
		}
		
		return nameExists;
		
	}
	
	public Data getData(String name) {
		Data data = null;
		MessagePacketDB messageSending = new MessagePacketDB(name, null, "CHECK", null, true);
		try {
			socketOut.writeObject(messageSending);
		}
		catch (Exception e) {e.printStackTrace();}
		MessagePacketDB messageReceive = null;
		
		try {
			messageReceive = readSocketIn();
		}
		catch (Exception e) {}
		if (messageReceive.FROM == null && messageReceive.TO.equals(name)) {
			data = messageReceive.DATA;
		}
		return data;
	}
	
	private boolean createConnection(String serverAddress, int PORT) {
		boolean connected = false;
		try {
			socket = new Socket(serverAddress, PORT);
			socketOut = new ObjectOutputStream(socket.getOutputStream());
			socketIn = new ObjectInputStream(socket.getInputStream());
			connected = true;
		}
		catch (Exception e) {
			connected = false;
			e.printStackTrace();
		}
		
		return connected;
		
	}
	
}
