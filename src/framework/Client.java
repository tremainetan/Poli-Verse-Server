package framework;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import datastructures.MessagePacket;

public class Client {
	
	private String username;
	
	private Socket socket;
	private ObjectInputStream socketIn;
	private ObjectOutputStream socketOut;
	
	public Client(String serverAddress, String username, int PORT) {
		
		this.username = username;
		createConnection(serverAddress, PORT);
		
	}

	public void run() {
			
		////READ MESSAGES INCOMING
		try {
			Object obj = null;
			boolean hasObject = true;
			
			while (hasObject) {
				while (true) {
					try {
						obj = socketIn.readObject();
						hasObject = true;
						break;
					}
					catch (Exception e) {hasObject = false;}
				}
				
				//Object is Present
				//Unpack and Process Object
				MessagePacket messageReceive = (MessagePacket) obj;
				//Not Processed yet
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean createConnection(String serverAddress, int PORT) {
		boolean connected = false;
		try {
			socket = new Socket(serverAddress, PORT);
			socketOut = new ObjectOutputStream(socket.getOutputStream());
			socketOut.flush();
			socketIn = new ObjectInputStream(socket.getInputStream());
			MessagePacket submitName = new MessagePacket();
			submitName.FROM = username;
			submitName.TO = null;
			submitName.MESSAGEFILE = null;
			submitName.MESSAGESTRING = "SUBMITNAME";
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
