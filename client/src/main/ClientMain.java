package main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

import communication.ClientCommunication;
import database.ClientDatabase;
import datastructures.Data;
import framework.FileManager;
import framework.ProfanityLibrary;
import framework.Renderer;

public class ClientMain implements Runnable {
	private String serverAddress = "localhost";
	private int COMMUNICATION_PORT = 59001;
	private int DATABASE_PORT = 59002;
	
	private Random r;
	public Renderer renderer;
	public ClientCommunication clientCommunication;
	public ClientDatabase clientDatabase;
	public ArrayList<String> quotes = new ArrayList<String>();
	public Data clientData;
	
	public ArrayList<String> state = null;
	
	public ClientMain() {
		try {
			
			new ProfanityLibrary();
			clientDatabase = new ClientDatabase(serverAddress, DATABASE_PORT);
			
			renderer = new Renderer(this);
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
			renderer.print(getRandomQuote());
			renderer.textField.requestFocus();
			
			login();
			
			renderer.updateTitle("Poli-Verse (" + clientData.USERNAME + ")");
			clientCommunication = new ClientCommunication(this, renderer, serverAddress, clientData.USERNAME, COMMUNICATION_PORT);
			clientCommunication.init();
			
			run();
			
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public void run() {
		while (true) {
			while (!renderer.lastCommand.isBlank()) {
				//Process Every Command
				String command = renderer.lastCommand;
				if (state == null) {
					//Main State
					try {
						clientData = clientDatabase.getData(clientData.USERNAME);
					} catch (Exception e1) {}
					if (command.equals("help")) help();
					else if (command.equals("exit")) exit();
					else if (command.equals("listfriends")) listfriends();
					else if (command.equals("listrequests")) listrequests();
					else if (command.equals("listpending")) listpending();
					else if (command.equals("clearscreen")) renderer.clearScreen();
					else if (command.startsWith("request")) request(command);
					else if (command.startsWith("accept")) accept(command);
					else if (command.startsWith("reject")) reject(command);
					else if (command.startsWith("unfriend")) unfriend(command);
					else if (command.startsWith("chat")) chat(command);
					else renderer.print("Invalid Command, type 'help' for possible commands");
				}
				else {
					//Texting Someone
					if (command.startsWith("/")) {
						//Command
						String actualCommand = command.substring(1);
						if (actualCommand.equals("back")) {
							renderer.updateTitle("Poli-Verse (" + clientData.USERNAME + ")");
							renderer.clearScreen();
							state = null;
						}
						else if (actualCommand.equals("clearscreen")) renderer.clearScreen();
						else if (actualCommand.equals("help")) {
							renderer.print("");
							renderer.print("Commands Available:");
							renderer.print("/back - Back to Home Screen");
							renderer.print("/clearscreen - Clears Current Screen");
							renderer.print("");
						}
					}
					else {
						//Message to send to "<name>"
						try {
							if (ProfanityLibrary.containsProfanity(command)) {
								command = "THIS MESSAGE CONTAINS A PROFANITY!!!";
								System.out.println(clientData.SCORE);
								clientData.SCORE -= 10;
								clientDatabase.updateData(clientData);
								System.out.println(clientData.SCORE);
								if (clientData.SCORE <= 0) {
									renderer.print("Your score has reached 0, your account will be terminated...");
									clientDatabase.ban(clientData.USERNAME);
									Thread.sleep(3000);
									exit();
								}
							}
							String message = clientData.USERNAME + ": " + command;
							renderer.print(message);
							clientCommunication.sendMessage(clientData.USERNAME, state, message);
							clientDatabase.sendMessage(clientData.USERNAME, state, message);
						} catch (Exception e) {}
					}
				}
				renderer.lastCommand = "";
			}
		}
	}
	
	//START OF COMMAND FUNCTIONS///
	
	private void help() {
		renderer.print("");
		renderer.print("Commands Available:");
		renderer.print("/exit - Leave the Application");
		renderer.print("/listfriends - List Current Friends");
		renderer.print("/listrequests - List People you are Waiting to Accept");
		renderer.print("/listpending - List People Waiting for you to Accept");
		renderer.print("/clearscreen - Clears Current Screen");
		renderer.print("/accept <name> - Accept a friend's Request");
		renderer.print("/reject <name> - Reject a friend's Request");
		renderer.print("/request <name> - Request to add a friend");
		renderer.print("/unfriend <name> - Unfriend a friend");
		renderer.print("/chat <name> - Enter Chat mode with a friend");
		renderer.print("");
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
	
	private void listfriends() {
		ArrayList<String> friends = clientData.FRIENDS;
		if (friends.size() == 0) renderer.print("You currently do not have any friends yet");
		else {
			for (int i = 0; i < friends.size(); i++) renderer.print(friends.get(i));
		}
	}
	
	private void listrequests() {
		ArrayList<String> requests = clientData.REQUESTS;
		if (requests.size() == 0) renderer.print("No one has requested to follow you yet");
		else {
			renderer.print("You have " + requests.size() + " requests:");
			for (int i = 0; i < requests.size(); i++) renderer.print(requests.get(i));
		}
	}
	
	private void listpending() {
		ArrayList<String> pending = clientData.PENDING;
		if (pending.size() == 0) renderer.print("You have no pending requests currently");
		else {
			for (int i = 0; i < pending.size(); i++) renderer.print(pending.get(i));
		}
	}
	
	
	private void request(String command) {
		String requestedName = command.substring(8);
		boolean nameExists = false;
		try {
			nameExists = clientDatabase.nameExists(requestedName);
		} catch (Exception e) {}
		try {
			if (clientData.USERNAME != requestedName && nameExists) {
				clientData.PENDING.add(requestedName);
				clientDatabase.updateData(clientData);
				clientDatabase.request(clientData.USERNAME, requestedName);
				renderer.print("Request Successfully sent to " + requestedName);
			}
			else {
				renderer.print("Sending Request Failed...Check that the username exists and is not yourself");
			}
		}
		catch (Exception e) {
			renderer.print("Sending Request Failed...Check that the username exists and is not yourself");
		}
	}
	
	private void accept(String command) {
		String acceptName = command.substring(7);
		try {
			if (clientData.REQUESTS.contains(acceptName)) {
				//This username is inside of REQUESTS list
				clientData.FRIENDS.add(acceptName);
				clientDatabase.accept(clientData.USERNAME, acceptName);
				clientData.REQUESTS.remove(acceptName);
				clientDatabase.updateData(clientData);
				renderer.print("Successfully Accepted " + acceptName + " as friend!");
			}
			else renderer.print("Accept Failed...Check that this user requested to follow you and is not yourself");
		}
		catch (Exception e) {
			renderer.print("Accept Failed...Check that this user requested to follow you and is not yourself");
		}
	}
	
	private void reject(String command) {
		String rejectName = command.substring(7);
		try {
			if (clientData.REQUESTS.contains(rejectName)) {
				clientDatabase.reject(clientData.USERNAME, rejectName);
				clientData.REQUESTS.remove(rejectName);
				clientDatabase.updateData(clientData);
				renderer.print("Successfully Rejected " + rejectName + "!");
			}
		}
		catch (Exception e) {
			renderer.print("Rejection Failed...Check that this user requested to follow you and is not yourself");
		}
	}
	
	private void unfriend(String command) {
		String unfriendName = command.substring(9);
		try {
			if (unfriendName != clientData.USERNAME && clientData.FRIENDS.contains(unfriendName)) {
				clientData.FRIENDS.remove(unfriendName);
				clientDatabase.updateData(clientData);
				clientDatabase.unfriend(clientData.USERNAME, unfriendName);
				renderer.print("Successfully Unfriended " + unfriendName + "!");
			}
			else {
				renderer.print("Unfriend Failed...Check that the username is your friend and is not yourself");
			}
		}
		catch (Exception e) {renderer.print("Unfriend Failed...Check that the username is your friend and is not yourself");}
	}
	
	private void chat(String command) {
		String name = command.substring(5);
		if (clientData.FRIENDS.contains(name)) {
			ArrayList<String> list = new ArrayList<String>();
			list.add(name);
			state = list;
			ArrayList<String> CONVERSATION = null;
			ArrayList<String> USERS = new ArrayList<String>();
			USERS.add(clientData.USERNAME);
			USERS = state;
			try {
				CONVERSATION = clientDatabase.getConversation(clientData.USERNAME, USERS);
			} catch (Exception e) {}
			renderer.updateTitle("Poli-Verse (" + clientData.USERNAME + ") [Chatting with " + state + "]");
			renderer.clearScreen();
			for (int i = 0; i < CONVERSATION.size(); i++) renderer.print(CONVERSATION.get(i));
		}
		else renderer.print(name + " is not identified as your friend");
	}
	
	///END OF COMMAND FUNCTIONS///
	
	///START OF CHAT FUNCTIONS///
	
	
	
	///END OF CHAT FUNCTIONS///
	
	private void login() throws Exception {
		
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
				if (clientDatabase.nameExists(usernameInput)) {
					renderer.print("This username has been used");
				}
				else {
					clientDatabase.register(usernameInput, passwordInput);
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
	
	public String getRandomQuote() {
		String quote = "Poli-Verse: " + quotes.get(r.nextInt(quotes.size()));
		return quote;
	}
	
	public static void main(String[] args) {
		new ClientMain();
	}
	
}
