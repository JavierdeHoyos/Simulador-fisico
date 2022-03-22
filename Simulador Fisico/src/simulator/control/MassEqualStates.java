package simulator.control;

import org.json.JSONObject;

public class MassEqualStates implements StateComparator{

	@Override
	public boolean equal(JSONObject s1, JSONObject s2) {
		
		int i = 0;
		boolean equal = true;
		
		if( s1.getInt("time") == s2.getInt("time")){
			
			if(s1.getJSONArray("bodies").length() == s2.getJSONArray("bodies").length()) {
				
				while(i < s1.getJSONArray("bodies").length() && equal == true) {
					
						if(s1.getJSONArray("bodies").getJSONObject(i).getString("id").equals(s2.getJSONArray("bodies").getJSONObject(i).getString("id"))) {
							
						
							if(s1.getJSONArray("bodies").getJSONObject(i).getDouble("m") == s2.getJSONArray("bodies").getJSONObject(i).getDouble("m")){
								
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