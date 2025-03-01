package framework;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
	
	public static boolean signedUp() throws Exception {
		File f = new File("res/SIGNEDUP.ini");
		return f.exists();
	}
	
	public static void writeSignedUp(boolean signedUp) {
		File f = new File("res/SIGNEDUP.ini");
		if (signedUp) {
			if (!f.exists()) {
				try {
					f.createNewFile();
				} catch (IOException e) {}
			}
		}
		else {
			if (f.exists()) f.delete();
		}
	}
		
}
