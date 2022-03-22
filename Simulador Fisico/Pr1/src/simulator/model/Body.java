package simulator.model;

import org.json.JSONObject;

import simulator.misc.Vector2D;

import java.lang.Object;

public class Body {
	
	protected String id;
	
	protected Vector2D v;
	
	protected Vector2D f;
	
	protected Vector2D p;

	
	protected double m;
	
	public Body(String id, Vector2D v, Vector2D p, double m) {
		
		this.id = id;
		this.v = v;
		this.p = p;
		this.m = m;
		f = new Vector2D();

		
	}
	
	void addForce(Vector2D force) {
		f = this.f.plus(force);
	}
	
	void resetForce() {
		f = new Vector2D();
	}
	
	void move(double t) {
		
		 Vector2D a;
		
		if(m == 0)
		{
			a = new Vector2D();
		}
		else
		{
			a = new Vector2D(f.scale(1/m));
		}
		
		
		f.scale(1.0 / m);
		  p.plus(v.scale(t).plus(a.scale(0.5 * t * t)));
		  v.plus(a.scale(t));
		
		
	}
	
	

	
	public JSONObject getState() {
		
JSONObject obj = new JSONObject();
		
		obj.put("id",this.id);
		obj.put("m", this.m);

		obj.put("p",getPosition().asJSONArray());
		obj.put("v",getVelocity().asJSONArray());
		obj.put("f",getForce().asJSONArray());
		
		return obj;
	}

	@Override
	public String toString() {
		return getState().toString();
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Body other = (Body) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	
	//GETTERS***********************************************************************************************
	

	public Vector2D getVelocity() {
		return this.v;
	}
	
	public Vector2D getForce() {
		return this.f;
	}
	
	public Vector2D getPosition() {
		return this.p;
	}
	
	public double getMass() {
	
		return this.m;
	}
	
	public void setMass(double mass) {
		
		m = mass;
		
	}
	
}