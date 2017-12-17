package jus.poc.prodcons.v6;

import java.util.Date;

import jus.poc.prodcons.Message;

public class MessageX implements Message {
	private String message;
	private long creation_ts;
	private long put_ts;
	
	public String getMessage() {
		return message;
	}

	public MessageX (String m) {
		this.message = m;
	}
	
	public String toString() {
		return message;
	}
	
	public void setCreationTimestamp(long ts) {
		this.creation_ts = ts;
	}
	
	public void setPutTimestamp(long ts) {
		this.put_ts = ts;
	}
}
