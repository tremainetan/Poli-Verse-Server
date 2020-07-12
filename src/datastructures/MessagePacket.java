package datastructures;

import java.io.File;
import java.io.Serializable;

public class MessagePacket implements Serializable {

	private static final long serialVersionUID = -5058303681552590968L;
	
	public String FROM = "";
	public String TO = null;
	public String MESSAGESTRING = null;
	public File MESSAGEFILE = null;
	
	public void resetValues() {
		FROM = "";
		TO = "";
		MESSAGESTRING = null;
		MESSAGEFILE = null;
	}
	
}
