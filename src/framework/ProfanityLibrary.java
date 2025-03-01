package framework;

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ProfanityLibrary {
	
	public static ArrayList<String> PROFANITY = new ArrayList<String>();
	
	public ProfanityLibrary() {
		File f = new File("res/PROFANITIES.ini");
		Scanner in = null;
		try {
			in = new Scanner(f);
		} catch (FileNotFoundException e) {e.printStackTrace();}
		while (in.hasNextLine()) {
			PROFANITY.add(in.nextLine());
		}
		in.close();
	}
	
	public static boolean containsProfanity(String string) {
		boolean contains = false;
		boolean shouldBreak = false;
		
		String list[] = string.split(" ");
		List<String> arrayList = new ArrayList<String>();
		arrayList = Arrays.asList(list);
		
		while (!shouldBreak) {
			for (int a = 0; a < PROFANITY.size(); a++) {
				String profanity = PROFANITY.get(a);
				for (int b = 0; b < arrayList.size(); b++) {
					String actualString = arrayList.get(b);
					if (actualString.equalsIgnoreCase(profanity)) {
						contains = true;
						shouldBreak = true;
					}
				}
			}
			shouldBreak = true;
		}
		
		return contains;
	}
	
}
