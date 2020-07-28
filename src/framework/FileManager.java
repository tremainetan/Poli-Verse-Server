package framework;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import datastructures.Data;
import datastructures.Database;

public class FileManager {
	
	public static Map<String, Data> data = new HashMap<String, Data>();
	
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
		data = database.DATA;
		
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
		database.DATA = data;
		try {
			writeDatabase(database);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//Methods that other Classes need to use
	public static boolean nameExists(String name) {
		if (data.containsKey(name)) {
			return true;
		}
		else return false;
	}
	
	public static void addName(String name, String password) {
		data.put(name, new Data(name, password, new ArrayList<String>()));
		updateDatabase();
	}
	
}
