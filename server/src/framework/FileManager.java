package framework;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import datastructures.Data;
import datastructures.Database;

public class FileManager {
	
	public static Map<String, Data> DATABASE_DATA = new HashMap<String, Data>();
	public static Map<ArrayList<String>, ArrayList<String>> COMMUNICATION_DATA = new HashMap<ArrayList<String>, ArrayList<String>>();
	
	//Call at the start of ServerMain Program
	public FileManager() {
		
		try {
			readDatabase();
		} catch (Exception e) {e.printStackTrace();}
		
	}
	
	//Private Processing Methods
	private static void readDatabase() throws Exception {
		Database database = null;
		FileInputStream fileIn;
		ObjectInputStream in;
		
		try {
			fileIn = new FileInputStream("res/DATABASE.ser");
		}
		catch (Exception e) {
			//File not Found
			updateDatabase();
			fileIn = new FileInputStream("res/DATABASE.ser");
		}
		in = new ObjectInputStream(fileIn);
		database = (Database) in.readObject();
		DATABASE_DATA = database.DATA;
		COMMUNICATION_DATA = database.CONVERSATIONS;
		
		in.close();
		fileIn.close();
	}
	
	private static void writeDatabase(Database data) throws Exception {
		
		FileOutputStream fileOut = new FileOutputStream("res/DATABASE.ser");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(data);
		out.close();
		fileOut.close();
		
	}
	
	private static void updateDatabase() {
		Database database = new Database();
		database.DATA = DATABASE_DATA;
		database.CONVERSATIONS = COMMUNICATION_DATA;
		try {
			writeDatabase(database);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//Methods that other Classes need to use
	public static boolean nameExists(String name) {
		if (DATABASE_DATA.containsKey(name)) {
			return true;
		}
		else return false;
	}
	
	public static void addName(String name, String password) {
		DATABASE_DATA.put(name, new Data(name, password, 50, new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>()));
		updateDatabase();
	}
	
	public static void updateData(Data userData) {
		DATABASE_DATA.put(userData.USERNAME, userData);
		updateDatabase();
	}
	
	public static ArrayList<String> getConversation(ArrayList<String> USERS) {
		try {
			readDatabase();
		} catch (Exception e) {e.printStackTrace();}
		Collections.sort(USERS);
		if (!COMMUNICATION_DATA.containsKey(USERS)) {
			COMMUNICATION_DATA.put(USERS, new ArrayList<String>());
			updateDatabase();
		}
		ArrayList<String> CONTENTS = COMMUNICATION_DATA.get(USERS);
		return CONTENTS;
	}
	
	public static void addLine(ArrayList<String> USERS, String line) {
		Collections.sort(USERS);
		ArrayList<String> CONVERSATION = getConversation(USERS);
		CONVERSATION.add(line);
		COMMUNICATION_DATA.replace(USERS, CONVERSATION);
		updateDatabase();
	}
	
	public static void ban(String name) {
		
		//Remove this user from any of the user's friends
		Set<String> peopleRelated = new HashSet<String>();
		Data data = DATABASE_DATA.get(name);
		for (int i = 0; i < data.FRIENDS.size(); i++) peopleRelated.add(data.FRIENDS.get(i));
		for (int i = 0; i < data.PENDING.size(); i++) peopleRelated.add(data.PENDING.get(i));
		for (int i = 0; i < data.REQUESTS.size(); i++) peopleRelated.add(data.REQUESTS.get(i));
		for (String personRelated : peopleRelated) {
			Data personData = DATABASE_DATA.get(personRelated);
			if (personData.FRIENDS.contains(name)) personData.FRIENDS.remove(name);
			if (personData.PENDING.contains(name)) personData.PENDING.remove(name);
			if (personData.REQUESTS.contains(name)) personData.REQUESTS.remove(name);
		}
		
		//Remove this user's data from database
		DATABASE_DATA.remove(name);
		
		//Delete Conversations
		for (Entry<ArrayList<String>, ArrayList<String>> entry : COMMUNICATION_DATA.entrySet()) {
			ArrayList<String> participants = entry.getKey();
			if (participants.contains(name)) {
				if (participants.size() == 2) {
					//Private Chat
					COMMUNICATION_DATA.remove(participants);
				}
				else {
					//Group Chat
					ArrayList<String> conversation = entry.getValue();
					participants.remove(name);
					COMMUNICATION_DATA.remove(participants);
					COMMUNICATION_DATA.put(participants, conversation);
				}
			}
		}
		updateDatabase();
	}
	
}
