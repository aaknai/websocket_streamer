package it.fabaris.websocket.client;

import java.net.URI;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;


public class Test {
	public static void main(String[] args) throws Exception {
		String destUri = "ws://portal.mscoe.it:6180/arcgis/ws/services/smart_airfield/fluel-tank-stream/StreamServer/subscribe";
        if (args.length > 0)
        {
            destUri = args[0];
        }

        WebSocketClient client = new WebSocketClient();
        SimpleWebsocket socket = new SimpleWebsocket();
        try
        {
            client.start();

            URI echoUri = new URI(destUri);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(socket,echoUri,request);
            System.out.printf("Connecting to : %s%n",echoUri);

            // wait for closed socket connection.
            //socket.awaitClose(Integer.MAX_VALUE,TimeUnit.SECONDS);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        finally
        {
            try
            {
                client.stop();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
	}
}
