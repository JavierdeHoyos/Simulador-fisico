package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class BuilderBasedFactory <T> implements Factory<T>{

	private List<Builder<T>> _builders;
	List<JSONObject> factoryElements;
	
	
	public BuilderBasedFactory(List<Builder<T>> builders) {
		
		_builders = new ArrayList<Builder<T>>(builders);
		factoryElements = new ArrayList<JSONObject>();
		
		for(Builder<T> a: builders) {
			factoryElements.add(a.getBuilderInfo());
		}
		
	}
	
	@Override
	public T createInstance(JSONObject info) {
		
		T b = null;
		
		if(info == null) throw
			new IllegalArgumentException("Invalid value for create instance: null");
		
		
		for(Builder<T> a: _builders) {
			
			b = a.createInstance(info);
			
			if(b != null) {
				return b;
			}
			
		}
		
		throw new IllegalArgumentException("Invalid value for create instance: null");
		
	}

	@Override
	public List<JSONObject> getInfo() {
		return factoryElements;
		
	}

}
