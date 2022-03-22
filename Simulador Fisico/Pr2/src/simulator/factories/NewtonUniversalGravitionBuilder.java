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
		try {
		double G = data.has("G") ? data.getDouble("G")	 : 6.67E-11;	
		
		
		return new NewtonUniversalGravition(G);
		}
		catch(Exception e) {
			throw new IllegalArgumentException("valores no validos");
		}
	}

	@Override
	public JSONObject createData() {
		JSONObject data = new JSONObject();
		
		JSONObject aux = new JSONObject();
		
		aux.put("G", "the gravitational constant (a number)");
		
		data.put("type", "nlug");

		data.put("data", aux);
		
		data.put("desc", "Newton's Law of universal gravition");
		
		return data;
	}

	@Override
	public JSONObject getBuilderInfo() {
		
		return createData();
	}
}
