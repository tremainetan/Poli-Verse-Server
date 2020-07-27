package datastructures;

import java.io.File;
import java.io.Serializable;

public class MessagePacket implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public String FROM = null;
	public String TO = null;
	public String MESSAGESTRING = null;
	public File MESSAGEFILE = null;
	public boolean CONNECTED = true;
	
	public MessagePacket(String FROM, String TO, String STRING, File FILE, boolean CONNECTED) {
		this.FROM = FROM;
		this.TO = TO;
		this.MESSAGESTRING = STRING;
		this.MESSAGEFILE = FILE;
		this.CONNECTED = CONNECTED;
	}
	
}