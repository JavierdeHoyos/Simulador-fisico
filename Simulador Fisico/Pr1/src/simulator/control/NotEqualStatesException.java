package simulator.control;

import org.json.JSONObject;

public class NotEqualStatesException extends RuntimeException{
	
    private static final long serialVersionUID = 1L;

    private JSONObject _actual;
    private JSONObject _expected;
    private JSONObject _ob1;
    private JSONObject _ob2;
    private int _step;
    
    NotEqualStatesException(JSONObject exp,JSONObject act,JSONObject ob1,JSONObject ob2, int step){
    	super("States are ifferent at step" + step + System.lineSeparator()+   
    			"Actual: " + act + System.lineSeparator()+
    			"Expected: " + exp + System.lineSeparator()+
    			"Actual Body: " + ob1 + System.lineSeparator()+
    			"Expected Body: " + ob2 + System.lineSeparator()
    			);
    	
    	_actual = act;
    	_expected= exp;
    	_step=step;
    	_ob1= ob1;
    	_ob2= ob2;
    }
 public JSONObject getactual() {
	 return _actual;
 }
 
public JSONObject getExpected() {
	return _expected;
}

public JSONObject getBodyActual() {
	return _ob1;
	
}
public JSONObject getBodyExpected() {
	return _ob2;
	
}

public int getStep() {
	return _step;
}
}
