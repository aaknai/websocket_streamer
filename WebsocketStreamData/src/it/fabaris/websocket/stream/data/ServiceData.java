package it.fabaris.websocket.stream.data;

import java.io.Serializable;
import java.util.List;

public class ServiceData implements Serializable{
	private String serviceUrl;
	private List<String> messages;
	public ServiceData(String serviceUrl, List<String> messages) {
		this.serviceUrl = serviceUrl;
		this.messages = messages;
	}
	public String getServiceUrl() {
		return serviceUrl;
	}
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	public List<String> getMessages() {
		return messages;
	}
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
}
