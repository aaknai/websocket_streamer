package it.fabaris.websocket.client;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
 * Basic Echo Client Socket
 */
@WebSocket(maxMessageSize = 64 * 1024)
public class SimpleWebsocket
{
    private Session session;
    private ArrayList<WebsocketHandler> handlers = new ArrayList<>();

    public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	@OnWebSocketClose
    public void onClose(int statusCode, String reason)
    {
        for(WebsocketHandler h : handlers) h.onClose(statusCode, reason);
        this.session = null;
    }

    @OnWebSocketConnect
    public void onConnect(Session session)
    {
        this.session = session;
        for(WebsocketHandler h : handlers) h.onConnect(session);
    }

    @OnWebSocketMessage
    public void onMessage(String msg)
    {
        for(WebsocketHandler h : handlers) h.onMessageReceive(msg);
    }
    
    public void sendMessage(String msg)
    {
        try {
            Future<Void> fut;
            fut = session.getRemote().sendStringByFuture(msg);
			fut.get(2,TimeUnit.SECONDS);
	        for(WebsocketHandler h : handlers) h.onMessageSent(msg);
		} catch (Throwable e) {
			e.printStackTrace();
		}
    }
    public void addWebsocketHandler(WebsocketHandler handler){
    	this.handlers.add(handler);
    }
    public void removeWebsocketHandler(WebsocketHandler handler){
    	this.handlers.remove(handler);
    }
    public interface WebsocketHandler {
    	public void onConnect(Session session);
    	public void onClose(int statusCode, String reason);
    	public void onMessageSent(String msg);
    	public void onMessageReceive(String msg);
    }
}