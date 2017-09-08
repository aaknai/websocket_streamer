package it.fabaris.websocket.stream;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import it.fabaris.websocket.client.SimpleWebsocketEndpoint;
import it.fabaris.websocket.stream.data.ServiceData;
import it.fabaris.websocket.stream.data.ServiceFlatData;

public class DataStreamer implements Runnable {

	private List<ServiceFlatData> data = new ArrayList<>();
	private float speed;
	private LocalDateTime now;
	private Map<String, SimpleWebsocketEndpoint> endpoints = new HashMap<>();
	private WebsocketStreamDataWindow appWindow;
	private int step;
	private long lastRun;
	private DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss.SSS");
	public DataStreamer(List<ServiceData> services, float speed, WebsocketStreamDataWindow appWindow) {
		try {
			initializeData(services);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setSpeed(speed);
		this.appWindow = appWindow;
		this.step = 0;
	}
	public synchronized void setSpeed(float speed) {
		this.speed = speed;
	}
	public synchronized float getSpeed() {
		return this.speed;
	}
	private void initializeData(List<ServiceData> services) throws URISyntaxException, Exception {
		this.data.clear();
		for(ServiceData service : services){
			if(!endpoints.containsKey(service.getServiceUrl())) endpoints.put(service.getServiceUrl(), new SimpleWebsocketEndpoint(new URI(service.getServiceUrl())).connect());
			for(JSONObject o : service.getMessages()) this.data.add(new ServiceFlatData(service.getServiceUrl(), o, getDate(o)));
		}
		data.sort((a,b) -> {
			if(a.localDateTime.isAfter(b.localDateTime)) return 1;
			else if(a.localDateTime.isBefore(b.localDateTime)) return -1;
			else return 0;
		});
		for(ServiceFlatData d : data) {
			System.out.println(d.localDateTime+":"+d.message.toString());
			break;
		}
	}
	private LocalDateTime getDate(JSONObject o) {
		JSONObject baseObject = Configuration.getInstance().getConfig().objectAttribute != null ? o.getJSONObject(Configuration.getInstance().getConfig().objectAttribute) : o;
		long millis = baseObject.getLong(Configuration.getInstance().getConfig().dateField);
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
	}
	public void closeEndpoints(){
		for(SimpleWebsocketEndpoint e : endpoints.values()){
			try {
				e.disconnect();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	@Override
	public void run() {
		if(data.size()==0)return; // no data no party;
		if(now == null) now = data.get(0).localDateTime.plusNanos(0);		
		long currentRun = System.nanoTime();
		long elapsedNanos = this.lastRun > 0 ? currentRun-lastRun : 0;
		lastRun = currentRun;
		now = now.plusNanos((long) (elapsedNanos*getSpeed()));
		appWindow.setTime(now, getSpeed());
		while(data.get(step).localDateTime.isBefore(now)){
			System.out.println("sending message...");
			endpoints.get(data.get(step).serviceUrl).sendMessage(data.get(step).message.toString());
			++step;
			if(step >= data.size()){
				now = null;
				step=0;
				break;
			}
		}
		appWindow.setStatus("message "+step+"/"+data.size()+" will be sent @ "+data.get(step).localDateTime.format(dateTimeFormat));
	}

}
