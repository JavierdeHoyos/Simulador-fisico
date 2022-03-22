package simulator.factories;

import org.json.JSONObject;

public abstract class Builder<T> {

	protected String _typeTag;
	protected String _Desc;
	
	public Builder(String _typeTag, String _Desc) {
		this._typeTag = _typeTag;
		this._Desc = _Desc;
	}

	public T createInstance(JSONObject info) {
		
		T b = null;
		
		if(_typeTag != null && _typeTag.equals(info.getString("type")))
			b = createTheInstance(info.getJSONObject("data"));
		
		return b;
		
	}
	
	protected abstract T createTheInstance(JSONObject jsonObject);
	
	
	public JSONObject getBuilderInfo() {
		
			JSONObject info = new JSONObject();
			info.put("type", _typeTag);
			info.put("data", createData());
			info.put("desc", _Desc);
			return info;
		
		
	}
	
	protected JSONObject createData() {
		return new JSONObject();
	}
	
}
