 package simulator.model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;


public class PhisicsSimulator {

	private double dt;
	
	private ForceLaws forceLaws;
	
	private double T;
	
	private List<Body> list;
	
	private List<SimulatorObserver> observer;
	
	public PhisicsSimulator(double dt, ForceLaws forceLaws)throws IllegalArgumentException{
		
		if(dt < 0.0)
		{
			throw new IllegalArgumentException("dt must be < 0");
		}
		
		if(forceLaws == null)
		{
			throw new IllegalArgumentException("forceLaws can not be null");
		}
		
		this.dt = dt;
		
		this.forceLaws = forceLaws;
		
		T = 0.0;
		
		this.list = new ArrayList<Body>();
		
		this.observer = new ArrayList<SimulatorObserver>();
	}
	public void addObserver(SimulatorObserver o) {
		if (!this.observer.contains(o)) {
			this.observer.add(o);
			o.onRegister(list, T, dt, forceLaws.toString());
		}
	}
	
	
	public void reset() {
		list.clear();
		T = 0.0;
		for (int i = 0; i < this.observer.size(); i++) {
			this.observer.get(i).onReset(list, T, dt, forceLaws.toString());
		}
	}
	
	public void setDeltaTime(double dt) {
		
		if(dt < 0.0)
		{
			throw new IllegalArgumentException("dt must be < 0");
		}
		this.dt = dt;
		
		for (int i = 0; i < this.observer.size(); i++) {
			this.observer.get(i).onDeltaTimeChanged(dt);
		}
	}
	
	public void setForceLaws(ForceLaws forceLaws) {
		if(forceLaws == null)
		{
			throw new IllegalArgumentException("forceLaws can not be null");
		}
		
		this.forceLaws = forceLaws;
		for(SimulatorObserver o:this.observer)
			o.onForceLawsChanged(forceLaws.toString());
	}
	
	public void advance() {
		
		for(Body b: list) {
			
			b.resetForce();
		}
			forceLaws.apply(list);
			
			for(Body b: list) {
				
					b.move(dt);
				}
			
			
			T += dt;
			
			for (int i = 0; i < this.observer.size(); i++) {
				this.observer.get(i).onAdvance(list, T);
			}
		
	}
	
	public void addBody(Body b) {
		
		if (!list.contains(b)) {
			list.add(b);
		}
		else {
			throw new IllegalArgumentException("body already added");
		}
		for (SimulatorObserver o : observer)
			o.onBodyAdded(list, b);
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
