package jus.poc.prodcons.v6;

import jus.poc.prodcons.Message;

public class MessageX implements Message {
	private String message;
	public int id;
	private long creation_ts;
	private long put_ts;
	private long get_ts;
	private long processing_ts;
	
	public String getMessage() {
		return message;
	}

	public MessageX (String m) {
		this.message = m;
	}
	
	public MessageX (int id, String m) {
		this.message = m;
		this.id = id;
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
	
	public void setGetTimestamp(long ts) {
		this.get_ts = ts;
	}
	
	public void setProcessingTimestamp(long ts) {
		this.processing_ts = ts;
	}
}