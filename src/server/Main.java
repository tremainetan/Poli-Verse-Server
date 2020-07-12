package server;

import java.net.ServerSocket;
import java.time.LocalDate;
import java.util.concurrent.Executors;

public class Main {
	
	private static int PORT = 59001;
	
	public static void main(String[] args) {
		LocalDate startDate = LocalDate.now();
		System.out.println("Poli-Verse Chat Server started | Date: " + startDate);
		var pool = Executors.newFixedThreadPool(500);
		try (var listener = new ServerSocket(PORT)) {
			while (true) {
				pool.execute(new ClientHandler(listener.accept()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
