package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class NewtonUniversalGravition implements ForceLaws{

	protected double _g;
	
	private double G;
	public NewtonUniversalGravition(double _G) {
		G = _G;
	}
	
	@Override
	public String toString() {
		return "NewtonUniversalGravition G=" + G;
	}

	@Override
	public void apply(List<Body> bs) {
		
		Vector2D F = new Vector2D();
		
		for(Body a: bs) {
			
			if(a.m != 0.0) {
				
				for(Body b: bs) {
				
					if(a != b)
					{
						F = F.plus(force(a, b));
					}
				}
			
			}
			a.addForce(F);
			F = new Vector2D();
		}
		
	}

	
	private Vector2D force(Body a, Body b) {
		
		 Vector2D delta = b.p.minus(a.p);
		    double dist = delta.magnitude();
		    double magnitude = dist>0 ? (G * a.m * b.m) / (dist * dist) : 0.0;
		    return delta.direction().scale(magnitude);
	
		
	}

}
