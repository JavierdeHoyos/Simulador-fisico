package simulator.control;



import java.io.InputStream;
import java.io.OutputStream;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.PhisicsSimulator;

public abstract class Controller {

	
	protected PhisicsSimulator phySim;
	private Factory<Body> builder;
	
	public Controller(PhisicsSimulator phySim, Factory<Body> builder) {
		
		this.phySim = phySim;
		
		this.builder = builder;
		
	}
	
	public void loadBodies(InputStream in) {
		JSONObject jsonInupt = new JSONObject(new JSONTokener(in));
		JSONArray bodies = jsonInupt.getJSONArray("bodies");
		for (int i = 0; i < bodies.length(); i++)
			phySim.addBody(builder.createInstance(bodies.getJSONObject(i)));
	}
	
	public abstract void run(int n, OutputStream out);
	
}

		
	
	

	
	
	


	

