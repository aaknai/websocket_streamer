package it.fabaris.websocket.stream.data;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ServiceDataXML")
public class ServiceDataXML implements Serializable{
	@XmlElement(name="service")
	public List<ServiceData> services;
}
