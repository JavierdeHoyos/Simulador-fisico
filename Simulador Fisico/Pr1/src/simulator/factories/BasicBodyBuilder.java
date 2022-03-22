package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body>{

	public BasicBodyBuilder() {
		super("basic", "Default Body");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Body createTheInstance(JSONObject data) {
		
		Vector2D p, v;
		
		String id = data.getString("id");
		
		v = new Vector2D(data.getJSONArray("v").getDouble(0), data.getJSONArray("v").getDouble(1));
		
		p = new Vector2D(data.getJSONArray("p").getDouble(0), data.getJSONArray("p").getDouble(1));
		
		double m = data.getDouble("m");
		
		return new Body(id, v, p, m);
	}

	@Override
	public JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("p", "the position");
		data.put("m", "the mass");
		data.put("id", "the identifier");
		data.put("v", "the velocity");
		return data;
	}
	
}
