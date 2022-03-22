package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.MassLossingBody;

public class MassLosingBodyBuilder extends Builder<Body>{

	public MassLosingBodyBuilder() {
		super("mlb", "Mass losing Body");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Body createTheInstance(JSONObject data) {
		String id = data.getString("id");
		
		double m = data.getDouble("m");
		
		Vector2D p = new Vector2D(data.getJSONArray("p").getDouble(0), data.getJSONArray("p").getDouble(1));
		
		Vector2D v = new Vector2D(data.getJSONArray("v").getDouble(0), data.getJSONArray("v").getDouble(1));
		
		double lossfactor = data.getDouble("factor");
		
		double lossFrequency = data.getDouble("freq");
		
		return new MassLossingBody( id,  v,  p,  m,  lossfactor,  lossFrequency);
		
		
		
	}

	@Override
	public JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("pos", "the position");
		data.put("mass", "the mass");
		data.put("id", "the identifier");
		data.put("vel", "the velocity");
		return data;
	}
	
}
