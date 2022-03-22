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
		
		double g;
		Vector2D c;
		
		if(data.has("g")) {
			g = data.getDouble("g");
		}
		else {
			g = 9.81;
		}
		
		if(data.has("c")) {
			c = new Vector2D(data.getJSONArray("c").getDouble(0), data.getJSONArray("c").getDouble(1));
		}
		else {
			c = new Vector2D();
		}
		
		return new MovingTowardsFixedPoint(c, g);
	}
	
	@Override
	protected JSONObject createData() {
		
		JSONObject data = new JSONObject();
		data.put("g", "the g");
		data.put("c", "the point");
		
		return data;
	}


	
	

}
