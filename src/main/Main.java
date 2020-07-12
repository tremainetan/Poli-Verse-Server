package main;

import java.util.ArrayList;
import java.util.Random;

import datastructures.Data;
import framework.Client;
import framework.FileManager;
import framework.Renderer;

public class Main {
	
	private String serverAddress = "localhost";
	private int PORT = 59001;
	
	private Random r;
	private Renderer renderer;
	private Client client;
	private ArrayList<String> quotes = new ArrayList<String>();
	
	private String USERNAME = null;
	private String PASSWORD = null;
	
	public Main() {
		
		try {
			
			this.renderer = new Renderer();
			this.client = new Client();
			this.r = new Random();
			this.quotes = FileManager.readTextFromFile("QUOTES.ini");
			
			renderer.createWindow();
			
			renderer.print("Welcome to Poli-Verse!");
			renderer.print(quotes.get(r.nextInt(quotes.size())));
			renderer.textField.requestFocus();
			
			getName();
			client.createConnection(serverAddress, PORT);
			
			loop();
			
			
		} catch (Exception e) {e.printStackTrace();}
		
	}
	
	private void loop() {
		
	}
	
	private void getName() throws Exception {
		
		Data data = FileManager.readData();
		boolean REMEMBERME = data.REMEMBERME;
		USERNAME = data.USERNAME;
		PASSWORD = data.PASSWORD;
		
		if (!REMEMBERME) {
			if (USERNAME == null) {
				//Sign Up
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
						REMEMBERME = true;
						doneRememberMe = true;
					}
					else if (REM_ME_COMMAND.equals("N")) {
						REMEMBERME = false;
						doneRememberMe = true;
					}
				}
			}
			else {
				
				boolean accepted = false;
				
				while (!accepted) {
					
					String usernameInput = "";
					String passwordInput = "";
					renderer.print("Username: ");
					while (usernameInput.isBlank()) {
						usernameInput = renderer.lastCommand;
					}
					renderer.lastCommand = "";
					renderer.print("Password: ");
					while (passwordInput.isBlank()) {
						passwordInput = renderer.lastCommand;
					}
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
		
	}
	
	public static void main(String[] args) {
		new Main();
	}
	
}
