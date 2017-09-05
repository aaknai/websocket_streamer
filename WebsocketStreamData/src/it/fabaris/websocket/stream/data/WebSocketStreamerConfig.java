package it.fabaris.websocket.stream.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="WebSocketStreamerConfig")
public class WebSocketStreamerConfig implements Serializable{
	@XmlElement(name="service")
	public List<ServiceData> services = new ArrayList<ServiceData>();
	public String objectAttribute="attributes";
	public String dateField="date";
}
