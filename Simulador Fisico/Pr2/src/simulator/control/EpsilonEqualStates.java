package simulator.control;

import org.json.JSONObject;

import simulator.misc.Vector2D;

public class EpsilonEqualStates implements StateComparator{

	private double eps;
	
	public EpsilonEqualStates(double eps) {
		this.eps = eps;
	}
	
	@Override
	public boolean equal(JSONObject s1, JSONObject s2) {
		
		int i = 0;
		boolean equal = true;
		
		Vector2D v1, v2;
		double m1, m2;
		String id1, id2; 
		
		if(s1.getInt("time") == s2.getInt("time")){
			
			if(s1.getJSONArray("bodies").length() == s2.getJSONArray("bodies").length())
			{
				while(i < s1.getJSONArray("bodies").length() && equal == true) {
					
					id1 = s1.getJSONArray("bodies").getJSONObject(i).getString("id");
					id2 = s2.getJSONArray("bodies").getJSONObject(i).getString("id");
					
					if(id1.equals(id2)) {
					
					v1 = new Vector2D(s1.getJSONArray("bodies").getJSONObject(i).getJSONArray("v").getDouble(0), s1.getJSONArray("bodies").getJSONObject(i).getJSONArray("v").getDouble(1));
					v2 = new Vector2D(s2.getJSONArray("bodies").getJSONObject(i).getJSONArray("v").getDouble(0), s2.getJSONArray("bodies").getJSONObject(i).getJSONArray("v").getDouble(1));
					
					if(v1.distanceTo(v2) <= eps) {
						
						v1 = new Vector2D(s1.getJSONArray("bodies").getJSONObject(i).getJSONArray("p").getDouble(0), s1.getJSONArray("bodies").getJSONObject(i).getJSONArray("p").getDouble(1));
						v2 = new Vector2D(s2.getJSONArray("bodies").getJSONObject(i).getJSONArray("p").getDouble(0), s2.getJSONArray("bodies").getJSONObject(i).getJSONArray("p").getDouble(1));
						
						if(v1.distanceTo(v2) <= eps) {
							
							m1 = s1.getJSONArray("bodies").getJSONObject(i).getDouble("m");
							m2 = s2.getJSONArray("bodies").getJSONObject(i).getDouble("m");
							
							double m = Math.abs(m1-m2);
							
							if(m<= eps) {
								
							}
							else {
								equal = false;
							}
						}
						else {
							equal = false;
						}
					}
					else {
						equal = false;
					}
				}
				else {
					equal = false;
					}
					++i;
				}
				
			}
			else {
				equal = false;
			}
		}
		else {
			equal = false;
		}
		
		return equal;
	}

}

