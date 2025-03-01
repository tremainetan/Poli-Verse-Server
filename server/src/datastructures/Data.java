package datastructures;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {

	private static final long serialVersionUID = -5058303681552590968L;
	
	public String USERNAME = null;
	public String PASSWORD = null;
	public int SCORE = 40;
	public ArrayList<String> FRIENDS = new ArrayList<String>();
	public ArrayList<String> PENDING = new ArrayList<String>();
	public ArrayList<String> REQUESTS = new ArrayList<String>();
	
	public Data(String USERNAME, String PASSWORD, int SCORE, ArrayList<String> FRIENDS, ArrayList<String> PENDING, ArrayList<String> REQUESTS) {
		this.USERNAME = USERNAME;
		this.PASSWORD = PASSWORD;
		this.SCORE = SCORE;
		this.FRIENDS = FRIENDS;
		this.PENDING = PENDING;
		this.REQUESTS = REQUESTS;
	}
	
}