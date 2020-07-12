package datastructures;

import java.io.Serializable;

public class Data implements Serializable {

	private static final long serialVersionUID = -5058303681552590968L;
	
	public boolean REMEMBERME = false;
	public String USERNAME = null;
	public String PASSWORD = null;
	
	public void resetValues() {
		REMEMBERME = false;
		USERNAME = null;
		PASSWORD = null;
	}
	
}
