package main;

import java.net.Socket;
import java.util.HashMap;

import communication.CommunicationServer;
import database.DatabaseServer;
import framework.FileManager;

public class ServerMain {
	
	public static HashMap<String, Socket> sockets = new HashMap<String, Socket>();
	
	public ServerMain() {
		
		//Initialise File Manager
		new FileManager();
		
		//Creating instance of Communication and Database Thread
		CommunicationServer communicationServer = new CommunicationServer();
		DatabaseServer databaseServer = new DatabaseServer();
		Thread communicationThread = new Thread(communicationServer);
		Thread databaseThread = new Thread(databaseServer);
		
		//Initiating Communication and Database Thread
		communicationThread.start();
		databaseThread.start();
		
	}
	
	public static void main(String[] args) {
		
		new ServerMain();
		
	}
	
}
