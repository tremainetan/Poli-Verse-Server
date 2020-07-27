package communication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import datastructures.MessagePacket;

public class ClientCommunication implements Runnable {
	
	private String username;
	
	private Socket socket;
	private ObjectInputStream socketIn;
	private ObjectOutputStream socketOut;
	
	public ClientCommunication(String serverAddress, String username, int PORT) {
		
		this.username = username;
		createConnection(serverAddress, PORT);
		
	}

	private MessagePacket readSocketIn() throws Exception {
		
		MessagePacket obj = null;
		obj = (MessagePacket) socketIn.readObject();
		return obj;
		
	}
	
	public void run() {
			
		////READ MESSAGES INCOMING
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
		MessagePacket messageSending = new MessagePacket(username, null, null, null, false);
		socketOut.writeObject(messageSending);
	}
	
	private boolean createConnection(String serverAddress, int PORT) {
		boolean connected = false;
		try {
			socket = new Socket(serverAddress, PORT);
			socketOut = new ObjectOutputStream(socket.getOutputStream());
			socketOut.flush();
			socketIn = new ObjectInputStream(socket.getInputStream());
			MessagePacket submitName = new MessagePacket(username, null, "SUBMITNAME", null, true);
			socketOut.writeObject(submitName);
			connected = true;
		}
		catch (Exception e) {
			connected = false;
			e.printStackTrace();
		}
		
		return connected;
		
	}
	
}
