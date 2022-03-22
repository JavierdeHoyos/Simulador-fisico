package simulator.factories;

import org.json.JSONObject;

import simulator.control.MassEqualStates;
import simulator.control.StateComparator;


public class MassEqualStatesBuilder extends Builder<StateComparator>{

	public MassEqualStatesBuilder() {
		super("masseq", "mass equal state");
		// TODO Auto-generated constructor stub
	}

	@Override
	public StateComparator createTheInstance(JSONObject jsonObject) {
		
		return new MassEqualStates();
	}
}