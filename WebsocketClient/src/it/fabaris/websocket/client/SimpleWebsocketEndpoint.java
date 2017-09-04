package it.fabaris.websocket.client;

import java.net.URI;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import it.fabaris.websocket.client.SimpleWebsocket.WebsocketHandler;

public class SimpleWebsocketEndpoint {
	private SimpleWebsocket websocket = new SimpleWebsocket();
    private final WebSocketClient client = new WebSocketClient();
    private URI uri;
	public SimpleWebsocketEndpoint(URI uri) {
			this.uri = uri;
	}
	public SimpleWebsocketEndpoint connect() throws Exception {
		client.start();
		client.connect(websocket, uri, new ClientUpgradeRequest());
		return this;
	}

    public SimpleWebsocketEndpoint addWebsocketHandler(WebsocketHandler handler){
    	this.websocket.addWebsocketHandler(handler);
    	return this;
    }
    public SimpleWebsocketEndpoint removeWebsocketHandler(WebsocketHandler handler){
    	this.websocket.removeWebsocketHandler(handler);
    	return this;
    }
	public SimpleWebsocketEndpoint disconnect() throws Exception {
		this.websocket.getSession().close();
		client.stop();
    	return this;
	}
	public SimpleWebsocketEndpoint sendMessage(String text) {
		this.websocket.sendMessage(text);
		return this;
	}
}
