package framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import datastructures.Data;

public class FileManager {
	
	public static ArrayList<String> readTextFromFile(String fileName) throws Exception {
		ArrayList<String> lines = new ArrayList<String>();
		File f = new File("res/" + fileName);
		Scanner in = new Scanner(f);
		while (in.hasNextLine()) {
			lines.add(in.nextLine());
		}
		in.close();
		return lines;
	}
	
	public static Data readData() throws Exception {
		Data data = null;
		FileInputStream fileIn;
		ObjectInputStream in;
		
		try {
			fileIn = new FileInputStream("res/DATA.ser");
		}
		catch (Exception e) {
			//File not Found
			writeData(new Data());
			fileIn = new FileInputStream("res/DATA.ser");
		}
		in = new ObjectInputStream(fileIn);
		data = (Data) in.readObject();
		in.close();
		fileIn.close();
		
		return data;
	}
	
	public static void writeData(Data data) throws Exception {
		
		FileOutputStream fileOut = new FileOutputStream("res/DATA.ser");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(data);
		out.close();
		fileOut.close();
		
	}
		
}
