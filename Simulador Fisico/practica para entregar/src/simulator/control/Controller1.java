package simulator.control;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONObject;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhisicsSimulator;

public class Controller1 extends Controller{

	public Controller1(PhisicsSimulator phySim, Factory<Body> builder, Factory <ForceLaws> laws) {
		super(phySim, builder, laws);
	}

	@Override
	public void run(int n, OutputStream out) {
		if(out == null) {
			out = new OutputStream() {
				@Override
				public void write(int b) throws IOException {}
			};
		}
		
		PrintStream p = new PrintStream(out);
		JSONObject currState = null;
		
		p.println("{");
		p.println("\"states\": [");
		
		for(int i = 0; i < n; ++i) {
			
			currState = phySim.getState();
			
				p.println("," + currState);
				phySim.advance();
			}
		
			p.println("]");
			p.println("}");
		
		
	}
		
	}
	
	


