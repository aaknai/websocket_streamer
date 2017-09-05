package it.fabaris.websocket.stream.data;

import java.time.LocalDateTime;

import org.json.JSONObject;

public class ServiceFlatData {
	public String serviceUrl;
	public JSONObject message;
	public LocalDateTime localDateTime;
	public ServiceFlatData(String serviceUrl, JSONObject message, LocalDateTime localDateTime) {
		this.serviceUrl = serviceUrl;
		this.message = message;
		this.localDateTime = localDateTime;
	}
}
