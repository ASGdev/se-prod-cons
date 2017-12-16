package jus.poc.prodcons.v1;

import jus.poc.prodcons.Message;

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
