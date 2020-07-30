package communication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import datastructures.MessagePacket;
import framework.Renderer;
import main.ClientMain;

public class ClientCommunication implements Runnable {
	
	private String username;
	
	private Socket socket;
	private ObjectInputStream socketIn;
	private ObjectOutputStream socketOut;
	
	private Renderer renderer;
	private ClientMain main;
	
	public ClientCommunication(ClientMain main, Renderer renderer, String serverAddress, String username, int PORT) {
		
		this.username = username;
		this.main = main;
		this.renderer = renderer;
		createConnection(serverAddress, PORT);
		
	}

	private MessagePacket readSocketIn() throws Exception {
		
		MessagePacket obj = null;
		obj = (MessagePacket) socketIn.readObject();
		return obj;
		
	}
	
	public void run() {
		try {
			MessagePacket messageReceive = readSocketIn();
			if (messageReceive.CONNECTED) {
				if (messageReceive.FROM != null && messageReceive.TO.equals(username)) {
					if (main.state.equals(messageReceive.FROM)) renderer.print(messageReceive.MESSAGESTRING);
				}
			}
		}
		catch (Exception e) {}
	}
	
	public void disconnect() throws Exception {
		MessagePacket messageSending = new MessagePacket(username, null, null, false);
		socketOut.writeObject(messageSending);
	}
	
	public void sendMessage(String FROM, String TO, String CONTENT) throws Exception {
		MessagePacket messageSending = new MessagePacket(FROM, TO, CONTENT, true);
		socketOut.writeObject(messageSending);
	}
	
	private boolean createConnection(String serverAddress, int PORT) {
		boolean connected = false;
		try {
			socket = new Socket(serverAddress, PORT);
			socketOut = new ObjectOutputStream(socket.getOutputStream());
			socketOut.flush();
			socketIn = new ObjectInputStream(socket.getInputStream());
			MessagePacket submitName = new MessagePacket(username, null, "SUBMITNAME", true);
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
