package jus.poc.prodcons.loris;

import jus.poc.prodcons.Message;
import java.util.Date;

public class MessageX implements Message{
private String message;
	
	public String getMessage() {
		return message;
	}

	public MessageX (String m) {
		this.message = m;
	}
	
	public String toString() {
		return message;
	}
}
