package database;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import datastructures.MessagePacket;

public class ClientDatabase implements Runnable {
	
	private Socket socket;
	private ObjectInputStream socketIn;
	private ObjectOutputStream socketOut;
	
	public ClientDatabase(String serverAddress, int PORT) {
		
		createConnection(serverAddress, PORT);
		
	}
	
	private MessagePacket readSocketIn() throws Exception {
		
		MessagePacket obj = null;
		obj = (MessagePacket) socketIn.readObject();
		return obj;
		
	}
	
	public void run() {
		
		MessagePacket messageReceive = null;
		try {
			messageReceive = readSocketIn();
		} catch (Exception e) {}
		//Temporary Processing:
		if (messageReceive != null) {
			System.out.println("Received message from " + messageReceive.FROM + ": " + messageReceive.MESSAGESTRING);
		}
		
	}
	
	public void disconnect() throws Exception {
		MessagePacket messageSending = new MessagePacket(null, null, null, null, false);
		socketOut.writeObject(messageSending);
	}
	
	public boolean nameExists(String name) throws Exception {
		boolean nameExists = true;
		
		MessagePacket messageSending = new MessagePacket(name, null, "CHECK", null, true);
		socketOut.writeObject(messageSending);
		
		MessagePacket messageReceive = readSocketIn();
		if (messageReceive.FROM == null && messageReceive.TO.equals(name)) {
			if (messageReceive.MESSAGESTRING.equals("true")) nameExists = true;
			else nameExists = false;
		}
		
		return nameExists;
		
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
