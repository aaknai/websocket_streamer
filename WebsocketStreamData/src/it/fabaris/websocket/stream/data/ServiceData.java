package it.fabaris.websocket.stream.data;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.json.JSONObject;

import it.fabaris.websocket.stream.adapters.JsonObjectAdapter;

public class ServiceData implements Serializable{
	private String serviceUrl;
	private List<JSONObject> msgs;
	public ServiceData() {
		
	}
	public ServiceData(String serviceUrl, List<JSONObject> messages) {
		this.serviceUrl = serviceUrl;
		this.msgs = messages;
	}
	public String getServiceUrl() {
		return serviceUrl;
	}
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	@XmlElement(name="sessage")
	@XmlElementWrapper(name="messages")
	@XmlJavaTypeAdapter(value=JsonObjectAdapter.class)
	public List<JSONObject> getMessages() {
		return msgs;
	}
	public void setMessages(List<JSONObject> messages) {
		this.msgs = messages;
	}
}
