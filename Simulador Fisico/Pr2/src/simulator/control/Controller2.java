package simulator.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhisicsSimulator;

public class Controller2 extends Controller{

	private InputStream expOut;
	private StateComparator cmp;
	public Controller2(PhisicsSimulator phySim, Factory<Body> builder, InputStream expOut, StateComparator cmp, Factory <ForceLaws> laws) {
		super(phySim, builder, laws);
		this.expOut = expOut;
		this.cmp = cmp;
	}

	

	@Override
	public void run(int n, OutputStream out) {
		
		JSONObject expOutJO = null;
		
		if(expOut != null) {
			expOutJO = new JSONObject(new JSONTokener(expOut));
		}
		
		if(out == null) {
			out = new OutputStream() {
				@Override
				public void write(int b) throws IOException {}
			};
		}
		
		PrintStream p = new PrintStream(out);
		
		p.println("{");
		p.println("\"states\": [");
		
		
		
		JSONObject currState = null;
		JSONObject expState = null;
		
		for(int i = 0; i < n; ++i) {
			
			currState = phySim.getState();
			
			p.println("," + currState);
				
				if(expOutJO != null) {
					expState= expOutJO.getJSONArray("states").getJSONObject(i);

					if(!cmp.equal(expState,currState)) {
						throw new NotEqualStatesException(expState,currState, currState, expState,i);
					}
				}
					phySim.advance();
			}
			
			
		
		
			p.println("]");
			p.println("}");
	}

}
