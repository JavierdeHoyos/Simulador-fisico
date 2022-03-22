package simulator.control;



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhisicsSimulator;
import simulator.model.SimulatorObserver;

public abstract class Controller {

	
	protected PhisicsSimulator phySim;
	protected Factory <ForceLaws> laws;
	private Factory<Body> builder;
	
	public Controller(PhisicsSimulator phySim, Factory<Body> builder, Factory <ForceLaws> laws) {
		
		this.phySim = phySim;
		
		this.builder = builder;
		this.laws = laws;
	}
	
	public void loadBodies(InputStream in) {
		JSONObject jsonInupt = new JSONObject(new JSONTokener(in));
		JSONArray bodies = jsonInupt.getJSONArray("bodies");
		for (int i = 0; i < bodies.length(); i++)
			phySim.addBody(builder.createInstance(bodies.getJSONObject(i)));
	}
	
	public abstract void run(int n, OutputStream out);
	
	public void reset() {
		phySim.reset();
	}
	public void setDeltaTime(double dt) {
		phySim.setDeltaTime(dt);
	}
	
	public void addObserver(SimulatorObserver o) {
		phySim.addObserver(o);
	}
	
	public void run(int n) {//********************************************************************
		for (int i = 0; i < n; i++)
			new OutputStream() {
			@Override
			public void write(int b) throws IOException {}
			};
		
			phySim.advance();
	}
	
	public List<JSONObject> getForceLawsInfo() {
		List<JSONObject> aux = new ArrayList <JSONObject>();

		aux = laws.getInfo();
		
		return aux;
	}

	public void setForceLaws(JSONObject info) {
		ForceLaws grLaws =  laws.createInstance(info);
		this.phySim.setForceLaws(grLaws);
	}
}


		
	
	

	
	
	


	

