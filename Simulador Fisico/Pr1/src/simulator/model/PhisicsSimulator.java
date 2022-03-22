package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


public class PhisicsSimulator {

	private double dt;
	
	private ForceLaws ForceLaws;
	
	private double T;
	
	private List<Body> list;
	
	public PhisicsSimulator(double dt, ForceLaws forceLaws)throws IllegalArgumentException{
		
		if(dt < 0.0)
		{
			throw new IllegalArgumentException("error");
		}
		
		if(forceLaws == null)
		{
			throw new IllegalArgumentException("error");
		}
		
		this.dt = dt;
		
		this.ForceLaws = forceLaws;
		
		T = 0.0;
		
		this.list = new ArrayList<Body>();
	}
	
	public void advance() {
		
		for(Body b: list) {
			
			b.resetForce();
		}
			ForceLaws.apply(list);
			
			for(Body b: list) {
				
					b.move(dt);
				}
			
			
			T += dt;
		
	}
	
	public void addBody(Body b) {
		
		if (!list.contains(b)) {
			list.add(b);
		}
		else {
			throw new IllegalArgumentException("error");
		}
		
	}
	
	public JSONObject getState() {
		
		JSONObject obj = new JSONObject();
		JSONArray array = new JSONArray();
		
		for(int i = 0; i < list.size(); i++) {
			array.put(list.get(i).getState());
		}
		
		obj.put("time", T);
		
		obj.put("bodies", array);
		return obj;
		
	}
	
	@Override
	public String toString() {
		return getState().toString();
	}
	
}
