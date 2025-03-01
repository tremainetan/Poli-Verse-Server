package communication;

import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.concurrent.Executors;

public class CommunicationServer implements Runnable {
	
	private static int PORT = 59001;
	
	public static HashMap<String, Socket> sockets = new HashMap<String, Socket>();

	public void run() {
		LocalDate startDate = LocalDate.now();
		System.out.println("Poli-Verse Chat Server started | Date: " + startDate);
		var pool = Executors.newFixedThreadPool(500);
		try (var listener = new ServerSocket(PORT)) {
			while (true) {
				pool.execute(new CommunicationHandler(listener.accept()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
