package it.unisa.metric.web.socket;

import com.google.gson.Gson;

public class SocketResponse {
	private String textResponse="";
	private String payload="";
	private SocketEventType eventType;	

	/**
	 * @param textResponse
	 * @param objectResponse
	 * @param eventType
	 */
	public SocketResponse(String textResponse, String payload, SocketEventType eventType) {
		this.textResponse = textResponse;
		this.payload= payload;
		this.eventType = eventType;
	}


	public SocketResponse(SocketEventType eventType) {
		this.eventType = eventType;
	}	
	

	public String getTextResponse() {
		return textResponse;
	}


	public String getPayload() {
		return payload;
	}


	public SocketEventType getEventType() {
		return eventType;
	}
	
	public String toJson() {
		Gson g = new Gson();
		return g.toJson(this);
	}

	
}
