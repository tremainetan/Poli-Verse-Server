package datastructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database implements Serializable {
	
	private static final long serialVersionUID = 4917693259855534841L;
	
	public Map<String, Data> DATA = new HashMap<String, Data>();
	//Username, UserData
	
	public Map<ArrayList<String>, ArrayList<String>> CONVERSATIONS = new HashMap<ArrayList<String>, ArrayList<String>>();
	//Users involved in the conversation, List of Lines
	
}
