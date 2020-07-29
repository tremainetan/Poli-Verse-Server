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
		
	}
	
	public void disconnect() throws Exception {
		MessagePacketDB messageSending = new MessagePacketDB(null, null, null, null, false);
		socketOut.writeObject(messageSending);
	}
	
	public boolean nameExists(String name) throws Exception {
		boolean nameExists = true;
		
		MessagePacketDB messageSending = new MessagePacketDB(name, null, "EXISTS", null, true);
		socketOut.writeObject(messageSending);
		
		MessagePacketDB messageReceive = readSocketIn();
		if (messageReceive.FROM == null && messageReceive.TO.equals(name)) {
			if (messageReceive.MESSAGESTRING.equals("true")) nameExists = true;
			else nameExists = false;
		}
		
		return nameExists;
		
	}
	
	public void register(String name, String password) throws Exception {
		MessagePacketDB messageSending = new MessagePacketDB(name, password, "REGISTER", null, true);
		socketOut.writeObject(messageSending);
	}
	
	public void updateData(Data data) throws Exception {
		MessagePacketDB messageSending = new MessagePacketDB(data.USERNAME, null, "UPDATEDATA", data, true);
		socketOut.writeObject(messageSending);
	}
	
	public void request(String FROM, String TO) throws Exception {
		Data toData = getData(TO);
		toData.REQUESTS.add(FROM);
		updateData(toData);
	}
	
	public void accept(String FROM, String TO) throws Exception {
		Data toData = getData(TO);
		toData.PENDING.remove(FROM);
		toData.FRIENDS.add(FROM);
		updateData(toData);
	}
	
	public void reject(String FROM, String TO) throws Exception {
		Data toData = getData(TO);
		toData.PENDING.remove(FROM);
		updateData(toData);
	}
	
	public void unfriend(String FROM, String TO) throws Exception {
		Data toData = getData(TO);
		toData.FRIENDS.remove(FROM);
		updateData(toData);
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
