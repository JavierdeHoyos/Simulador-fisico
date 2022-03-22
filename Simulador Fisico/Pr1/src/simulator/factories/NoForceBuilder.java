package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NoForce;

public class NoForceBuilder extends Builder<ForceLaws>{

	public NoForceBuilder() {
		super("nf", "no force");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ForceLaws createTheInstance(JSONObject jsonObject) {
		
		return new NoForce();
	}

}
