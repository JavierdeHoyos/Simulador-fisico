package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NewtonUniversalGravition;

public class NewtonUniversalGravitionBuilder extends Builder<ForceLaws>{

	public NewtonUniversalGravitionBuilder() {
		super("nlug", "Newton universal gravition");
	}

	@Override
	public ForceLaws createTheInstance(JSONObject data) {
		
		double g = data.has("g") ? data.getDouble("g")	 : 9.81;	
	
		
		return new NewtonUniversalGravition(g);
	}

	@Override
	public JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("g", "the g");

		return data;
	}


}
