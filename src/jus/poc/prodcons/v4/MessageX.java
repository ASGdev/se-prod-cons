package jus.poc.prodcons.v4;

import jus.poc.prodcons.Message;

public class MessageX implements Message{
	private String message;
	private int compteur = 1;
	
	public String getMessage() {
		return message;
	}

	public MessageX (String m) {
		this.message = m;
	}
	
	public MessageX(String m, int nb) {
		this.message = m;
		this.compteur = nb;
	}
	
	public String toString() {
		return message;
	}
}
