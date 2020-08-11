package database;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import datastructures.Data;
import datastructures.MessagePacketDB;

public class ClientDatabase {
	
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
	
	public void ban(String name) throws Exception {
		MessagePacketDB messageSending = new MessagePacketDB(name, null, "BAN", null, true);
		socketOut.writeObject(messageSending);
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
		if (messageReceive.FROM == null && messageReceive.TO.contains(name)) {
			if (messageReceive.MESSAGESTRING.equals("true")) nameExists = true;
			else nameExists = false;
		}
		
		return nameExists;
		
	}
	
	public void register(String name, String password) throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		list.add(password);
		MessagePacketDB messageSending = new MessagePacketDB(name, list, "REGISTER", null, true);
		socketOut.writeObject(messageSending);
	}
	
	public void updateData(Data data) throws Exception {
		Data dataSending = new Data(data.USERNAME, data.PASSWORD, data.SCORE, data.FRIENDS, data.PENDING, data.REQUESTS);
		MessagePacketDB messageSending = new MessagePacketDB(data.USERNAME, null, "UPDATEDATA", dataSending, true);
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
	
	public Data getData(String name) throws Exception {
		Data data = null;
		MessagePacketDB messageSending = new MessagePacketDB(name, null, "CHECK", null, true);
		socketOut.writeObject(messageSending);
		MessagePacketDB messageReceive = null;
		messageReceive = readSocketIn();
		if (messageReceive.FROM == null && messageReceive.TO.contains(name)) {
			data = (Data) messageReceive.OBJECT;
		}
		return data;
	}
	
	public ArrayList<String> getConversation(String name, ArrayList<String> USERS) throws Exception {
		MessagePacketDB messageSending = new MessagePacketDB(name, null, "GETCONVERSATION", USERS, true);
		socketOut.writeObject(messageSending);
		
		MessagePacketDB messageReceive = readSocketIn();
		@SuppressWarnings("unchecked")
		ArrayList<String> CONVERSATION = (ArrayList<String>) messageReceive.OBJECT;
		return CONVERSATION;
	}
	
	public void sendMessage(String FROM, ArrayList<String> TO, String MESSAGE) throws Exception {
		MessagePacketDB messageSending = new MessagePacketDB(FROM, TO, "SENDMESSAGE", MESSAGE, true);
		socketOut.writeObject(messageSending);
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
