package database;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.Executors;

public class DatabaseServer implements Runnable {
	
	private static int PORT = 59002;
	
	public static HashMap<String, Socket> sockets = new HashMap<String, Socket>();

	public void run() {
		var pool = Executors.newFixedThreadPool(500);
		try (var listener = new ServerSocket(PORT)) {
			while (true) {
				pool.execute(new DatabaseHandler(listener.accept()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
