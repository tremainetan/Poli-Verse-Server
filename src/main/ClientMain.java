package main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

import communication.ClientCommunication;
import database.ClientDatabase;
import datastructures.Data;
import framework.FileManager;
import framework.Renderer;

public class ClientMain {
	
	private String serverAddress = "localhost";
	private int COMMUNICATION_PORT = 59001;
	private int DATABASE_PORT = 59002;
	
	private Random r;
	public Renderer renderer;
	public ClientCommunication clientCommunication;
	public ClientDatabase clientDatabase;
	public ArrayList<String> quotes = new ArrayList<String>();
	
	private Data clientData;
	
	public ClientMain() {
		
		try {
			
			clientDatabase = new ClientDatabase(serverAddress, DATABASE_PORT);
			
			renderer = new Renderer();
			r = new Random();
			quotes = FileManager.readTextFromFile("QUOTES.ini");
			
			renderer.createWindow();
			renderer.frame.addWindowListener(
				new WindowAdapter() {
					public void windowClosing(WindowEvent windowEvent) {
						exit();
					}
				}
			);
			
			renderer.print("Welcome to Poli-Verse!");
			renderer.print(quotes.get(r.nextInt(quotes.size())));
			renderer.textField.requestFocus();
			
			getName();
			
			Thread databaseThread = new Thread(clientDatabase);
			databaseThread.start();
			clientCommunication = new ClientCommunication(serverAddress, clientData.USERNAME, COMMUNICATION_PORT);
			Thread communicationThread = new Thread(clientCommunication);
			communicationThread.start();
			
		} catch (Exception e) {e.printStackTrace();}
		
	}
	
	private void exit() {
		try {
			clientDatabase.disconnect();
			clientCommunication.disconnect();
		}
		catch (Exception e) {
			System.exit(0);
		}
		System.exit(0);
	}
	
	private void getName() throws Exception {
		
		String usernameInput;
		String passwordInput;
		
		Data data = null;
		
		if (!FileManager.signedUp()) {
			//Sign Up
			
			while (true) {
				
				usernameInput = "";
				passwordInput = "";
				
				renderer.print("Create Username: ");
				while (usernameInput.isBlank()) usernameInput = renderer.lastCommand;
				renderer.lastCommand = "";
				renderer.print("Create Password: ");
				while (passwordInput.isBlank()) passwordInput = renderer.lastCommand;
				renderer.lastCommand = "";
				if (clientDatabase.nameExists(usernameInput, passwordInput)) {
					renderer.print("This username has been used");
				}
				else {
					data = clientDatabase.getData(usernameInput);
					this.clientData = data;
					FileManager.writeSignedUp(true);
					break;
				}
			}
		}
		else {
			//Login
			
			while (true) {
				
				usernameInput = "";
				passwordInput = "";
				
				renderer.print("Username: ");
				while (usernameInput.isBlank()) usernameInput = renderer.lastCommand;
				renderer.lastCommand = "";
				renderer.print("Password: ");
				while (passwordInput.isBlank()) passwordInput = renderer.lastCommand;
				renderer.lastCommand = "";
				
				//Received a Non-Blank Username and Password
				//Check if it is correct
				data = clientDatabase.getData(usernameInput);
				if (data != null) {
					if (passwordInput.equals(data.PASSWORD)) break;
					else renderer.print("Wrong username or password");
				}
				else renderer.print("Wrong username or password");
			}
			
			this.clientData = data;
		}
		
		renderer.print("Successfully Logged In as " + clientData.USERNAME + "!");
		
	}
	
	public static void main(String[] args) {
		new ClientMain();
	}
	
}
