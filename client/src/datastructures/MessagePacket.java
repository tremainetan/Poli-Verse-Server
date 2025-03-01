package datastructures;

import java.io.Serializable;
import java.util.ArrayList;

public class MessagePacket implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public String FROM = null;
	public ArrayList<String> TO = null;
	public String MESSAGESTRING = null;
	public boolean CONNECTED = true;
	
	public MessagePacket(String FROM, ArrayList<String> TO, String STRING, boolean CONNECTED) {
		this.FROM = FROM;
		this.TO = TO;
		this.MESSAGESTRING = STRING;
		this.CONNECTED = CONNECTED;
	}
	
}