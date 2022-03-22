package simulator.factories;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws>{

	public MovingTowardsFixedPointBuilder() {
		super("mtfp", "Moving Towards Fixed Point");
		// TODO Auto-generated constructor stub
	}
	@Override
	protected ForceLaws createTheInstance(JSONObject data) {
		
		try {
		Vector2D c;
		
		double g = data.has("g") ? data.getDouble("g")	 : 9.81;
		
		if(data.has("c")) {
			c = new Vector2D(data.getJSONArray("c").getDouble(0), data.getJSONArray("c").getDouble(1));
		}
		else {
			c = new Vector2D();
		}
		
		return new MovingTowardsFixedPoint(c, g);
		}
		catch(Exception e) {
			throw new IllegalArgumentException("valores no validos");
		}
	}
	
	@Override
	protected JSONObject createData() {
	JSONObject data = new JSONObject();
		
		JSONObject aux = new JSONObject();
		
		aux.put("c", "the point towards which bodies move (a json list of 2 numbers, e.g,[100.0,50.0]");
		aux.put("g", "the length of the acceleration vector (a number)");
		data.put("type", "mtfp");

		data.put("data", aux);
		
		data.put("desc", "Moving Towards a Fixed point");
		
		return data;
	}


	@Override
	public JSONObject getBuilderInfo() {
		
		return createData();
	}
	

}
