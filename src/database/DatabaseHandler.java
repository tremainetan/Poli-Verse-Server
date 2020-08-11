package database;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

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
						MessagePacketDB messageSending = null;
						ArrayList<String> list = new ArrayList<String>();
						list.add(name);
						if (!FileManager.nameExists(name)) {
							messageSending = new MessagePacketDB(null, list, "false", null, true);
						}
						else messageSending = new MessagePacketDB(null, list, "true", null, true);
						socketOut.writeObject(messageSending);
					}
					else if (messageIncoming.MESSAGESTRING.equals("BAN")) {
						FileManager.ban(messageIncoming.FROM);
					}
					else if (messageIncoming.MESSAGESTRING.equals("REGISTER")) {
						String name = messageIncoming.FROM;
						String password = messageIncoming.TO.get(0);
						FileManager.addName(name, password);
					}
					else if (messageIncoming.MESSAGESTRING.equals("CHECK")) {
						//Getting Password
						String name = messageIncoming.FROM;
						Data data = FileManager.DATABASE_DATA.get(name);
						ArrayList<String> list = new ArrayList<String>();
						list.add(name);
						MessagePacketDB messageSending = new MessagePacketDB(null, list, null, data, true);
						socketOut.writeObject(messageSending);
					}
					else if (messageIncoming.MESSAGESTRING.equals("UPDATEDATA")) {
						FileManager.updateData((Data) messageIncoming.OBJECT);
					}
					else if (messageIncoming.MESSAGESTRING.equals("GETCONVERSATION")) {
						@SuppressWarnings("unchecked")
						Object OBJ = (ArrayList<String>) FileManager.getConversation((ArrayList<String>) messageIncoming.OBJECT);
						ArrayList<String> list = new ArrayList<String>();
						list.add(messageIncoming.FROM);
						MessagePacketDB messageSending = new MessagePacketDB(null, list, null, OBJ, true);
						socketOut.writeObject(messageSending);
					}
					else if (messageIncoming.MESSAGESTRING.equals("SENDMESSAGE")) {
						String message = (String) messageIncoming.OBJECT;
						ArrayList<String> USERS = messageIncoming.TO;
						USERS.add(messageIncoming.FROM);
						FileManager.addLine(USERS, message);
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
