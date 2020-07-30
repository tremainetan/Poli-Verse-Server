package datastructures;

import java.io.Serializable;

public class MessagePacketDB implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public String FROM = null;
	public String TO = null;
	public String MESSAGESTRING = null;
	public Object OBJECT = null;
	public boolean CONNECTED = true;
	
	public MessagePacketDB(String FROM, String TO, String STRING, Object OBJECT, boolean CONNECTED) {
		this.FROM = FROM;
		this.TO = TO;
		this.MESSAGESTRING = STRING;
		this.OBJECT = OBJECT;
		this.CONNECTED = CONNECTED;
	}
	
}