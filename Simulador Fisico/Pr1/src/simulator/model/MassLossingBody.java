package simulator.model;

import org.json.JSONObject;

import simulator.misc.Vector2D;

public class MassLossingBody extends Body{

	private double lossFactor;
	
	private double lossFrequency;
	
	private double accumulatedTime;
	
	public MassLossingBody(String id, Vector2D v, Vector2D p, double m, double lossFactor, double lossFrequency) {
		super(id, v,p, m);
		this.lossFactor = lossFactor;
		this.lossFrequency = lossFrequency;
		accumulatedTime = 0.0;
	}

	void move(double t) {
	super.move(t);	
	
	accumulatedTime += t;
	
	if(accumulatedTime >= lossFrequency) {
		
		this.m = this.m*(1-lossFactor);
		
		accumulatedTime = 0.0;
		
	}
	else {
		accumulatedTime += t;
	}
		
	
	
	}

	@Override
	public JSONObject getState() {
		
		JSONObject obj = super.getState();
		
		obj.put("lossFactor", lossFactor);
		obj.put("lossFrecuency", lossFrequency);
		
		return obj;
		
		
	}
}
