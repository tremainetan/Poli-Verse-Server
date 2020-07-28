package datastructures;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {

	private static final long serialVersionUID = -5058303681552590968L;
	
	public String USERNAME = null;
	public String PASSWORD = null;
	public ArrayList<String> FRIENDS = new ArrayList<String>();
	
	public Data(String USERNAME, String PASSWORD, ArrayList<String> FRIENDS) {
		this.USERNAME = USERNAME;
		this.PASSWORD = PASSWORD;
		this.FRIENDS = FRIENDS;
	}
	
}