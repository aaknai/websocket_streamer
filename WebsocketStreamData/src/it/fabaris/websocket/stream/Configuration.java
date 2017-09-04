package it.fabaris.websocket.stream;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import it.fabaris.websocket.stream.data.ServiceData;
import it.fabaris.websocket.stream.data.WebSocketStreamerConfig;

public class Configuration {
	private static JAXBContext ctx;
	private static Unmarshaller unmarshaller;
	private static Marshaller marshaller;
	static{
		 try {
			ctx = JAXBContext.newInstance(
				WebSocketStreamerConfig.class, 
				ServiceData.class
			);
			unmarshaller = ctx.createUnmarshaller();
			marshaller = ctx.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	public WebSocketStreamerConfig getConfig() {
		return config;
	}
	private static Configuration instance;
	private WebSocketStreamerConfig config;
	private Configuration() {
		config = new WebSocketStreamerConfig();
	}
	
	public synchronized static Configuration getInstance(){
		if(instance == null) instance = new Configuration();
		return instance;
	}

	public void save(String file){
		try {
			marshaller.marshal(this.config, new File(file));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	public void save(){
		save("WebsocketStreamerConfig.xml");
	}
	public WebSocketStreamerConfig load(String file){
		try {
			this.config = (WebSocketStreamerConfig) unmarshaller.unmarshal(new File(file));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return this.config;
	}
	public WebSocketStreamerConfig load(){
		return load("WebsocketStreamerConfig.xml");
	}
}
