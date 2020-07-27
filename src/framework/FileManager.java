package framework;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import datastructures.Names;

public class FileManager {
	
	public static ArrayList<String> NAMES = new ArrayList<>();
	
	//Call at the start of ServerMain Program
	public FileManager() {
		
		try {
			readToArray();
		} catch (Exception e) {e.printStackTrace();}
		
	}
	
	//Private Processing Methods
	private static void readToArray() throws Exception {
		Names names = null;
		FileInputStream fileIn;
		ObjectInputStream in;
		
		try {
			fileIn = new FileInputStream("res/NAMES.ser");
		}
		catch (Exception e) {
			//File not Found
			writeData(new Names());
			fileIn = new FileInputStream("res/NAMES.ser");
		}
		in = new ObjectInputStream(fileIn);
		names = (Names) in.readObject();
		NAMES = names.NAMES;
		
		in.close();
		fileIn.close();
	}
	
	private static void writeData(Names data) throws Exception {
		
		FileOutputStream fileOut = new FileOutputStream("res/NAMES.ser");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(data);
		out.close();
		fileOut.close();
		
	}
	
	private static void updateArray() {
		Names data = new Names();
		data.NAMES = NAMES;
		try {
			writeData(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//Methods that other Classes need to use
	public static boolean nameExists(String name) {
		if (NAMES.contains(name)) {
			return true;
		}
		else return false;
	}
	
	public static void addName(String name) {
		NAMES.add(name);
		updateArray();
	}
	
}
