package simulator.factories;
import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NoForce;

public class NoForceBuilder extends Builder<ForceLaws>{

	public NoForceBuilder() {
		super("nf", "No Force");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ForceLaws createTheInstance(JSONObject jsonObject) {
		
		return new NoForce();
	}
	
	
	@Override
	public JSONObject createData() { //Revisar esto
        JSONObject data = new JSONObject();	
        JSONObject aux = new JSONObject();	
		data.put("type", "nf");
		data.put("data", aux);
		data.put("desc","No Force");

		return data;
	}
	@Override
	public JSONObject getBuilderInfo() {
		
		return createData();
	}
}
