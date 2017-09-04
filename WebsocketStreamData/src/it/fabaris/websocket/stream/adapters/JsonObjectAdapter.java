package it.fabaris.websocket.stream.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.json.JSONObject;

public class JsonObjectAdapter extends XmlAdapter<String, JSONObject> {

	@Override
	public String marshal(JSONObject arg0) throws Exception {
		return arg0.toString();
	}

	@Override
	public JSONObject unmarshal(String arg0) throws Exception {
		return new JSONObject(arg0);
	}

}
