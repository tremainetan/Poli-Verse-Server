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
	private Renderer renderer;
	private ClientCommunication clientCommunication;
	private ClientDatabase clientDatabase;
	private ArrayList<String> quotes = new ArrayList<String>();
	
	private String USERNAME = null;
	private String PASSWORD = null;
	
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
						try {
							clientDatabase.disconnect();
							clientCommunication.disconnect();
						}
						catch (Exception e) {
							System.exit(0);
						}
						System.exit(0);
					}
				}
			);
			
			renderer.print("Welcome to Poli-Verse!");
			renderer.print(quotes.get(r.nextInt(quotes.size())));
			renderer.textField.requestFocus();
			
			getName();
			
			Thread databaseThread = new Thread(clientDatabase);
			databaseThread.start();
			clientCommunication = new ClientCommunication(serverAddress, USERNAME, COMMUNICATION_PORT);
			Thread communicationThread = new Thread(clientCommunication);
			communicationThread.start();
			
			
		} catch (Exception e) {e.printStackTrace();}
		
	}
	
	private void getName() throws Exception {
		
		Data data = FileManager.readData();
		USERNAME = data.USERNAME;
		PASSWORD = data.PASSWORD;
		
		if (!data.REMEMBERME) {
			if (USERNAME == null) {
				//Sign Up
				boolean nameAccepted = false;
				
				while (!nameAccepted) {
					boolean doneRememberMe = false;
					USERNAME = "";
					PASSWORD = "";
					String REM_ME_COMMAND = "";
					renderer.print("Create Username: ");
					while (USERNAME.isBlank()) USERNAME = renderer.lastCommand;
					renderer.lastCommand = "";
					renderer.print("Create Password: ");
					while (PASSWORD.isBlank()) PASSWORD = renderer.lastCommand;
					renderer.lastCommand = "";
					while (!doneRememberMe) {
						renderer.print("Remember Particulars? (Y/N)");
						while (REM_ME_COMMAND.isBlank()) REM_ME_COMMAND = renderer.lastCommand;
						renderer.lastCommand = "";
						if (REM_ME_COMMAND.equals("Y")) {
							data.REMEMBERME = true;
							doneRememberMe = true;
						}
						else if (REM_ME_COMMAND.equals("N")) {
							data.REMEMBERME = false;
							doneRememberMe = true;
						}
					}
					if (clientDatabase.nameExists(USERNAME)) {
						renderer.print("This username has been used");
					}
					else nameAccepted = true;
					
				}
				
				//Remember Me Saved Directly
				data.USERNAME = USERNAME;
				data.PASSWORD = PASSWORD;
				FileManager.writeData(data);
				
			}
			else {
				
				boolean accepted = false;
				
				while (!accepted) {
					
					String usernameInput = "";
					String passwordInput = "";
					renderer.print("Username: ");
					while (usernameInput.isBlank()) usernameInput = renderer.lastCommand;
					renderer.lastCommand = "";
					renderer.print("Password: ");
					while (passwordInput.isBlank()) passwordInput = renderer.lastCommand;
					renderer.lastCommand = "";
					
					//Received a Non-Blank Username and Password
					//Check if it is correct
					
					if (usernameInput.equals(USERNAME) && passwordInput.equals(PASSWORD)) {
						accepted = true;
					}
					else {
						renderer.print("Wrong username or password");
					}
					
				}
				
			}
		}
		
		renderer.print("Successfully Logged In as " + USERNAME + "!");
		
	}
	
	public static void main(String[] args) {
		new ClientMain();
	}
	
}
