package framework;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
	
	private Socket socket;
	private ObjectInputStream socketIn;
	private ObjectOutputStream socketOut;
	
	public boolean createConnection(String serverAddress, int PORT) {
		boolean connected = false;
		try {
			socket = new Socket(serverAddress, PORT);
			socketOut = new ObjectOutputStream(socket.getOutputStream());
			socketOut.flush();
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
