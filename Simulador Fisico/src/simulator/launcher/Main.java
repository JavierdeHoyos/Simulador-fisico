
package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;


import simulator.control.Controller;
import simulator.control.Controller1;
import simulator.control.Controller2;
import simulator.control.StateComparator;
import simulator.factories.BasicBodyBuilder;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.EpsilonEqualStatesBuilder;
import simulator.factories.Factory;
import simulator.factories.MassEqualStatesBuilder;
import simulator.factories.MassLosingBodyBuilder;
import simulator.factories.MovingTowardsFixedPointBuilder;
import simulator.factories.NewtonUniversalGravitionBuilder;
import simulator.factories.NoForceBuilder;
import simulator.model.Body;
import simulator.model.ForceLaws;

import simulator.model.PhisicsSimulator;

import simulator.view.MainWindow;

public class Main {

	// default values for some parameters
	//
	private final static Double _dtimeDefaultValue = 2500.0;
	private final static String _forceLawsDefaultValue = "mtfp";
	private final static String _stateComparatorDefaultValue = "epseq";
	private final static Integer _stepsDefaultValue = 150;

	// some attributes to stores values corresponding to command-line parameters
	//
	private static int _steps;
	private static Double _dtime = null;
	private static String _inFile = null;
	private static String _outFile = null;
	private static String _expOutFile  = null;
	private static JSONObject _forceLawsInfo = null;
	private static JSONObject _stateComparatorInfo = null;
	private static String _mode = null;
	
	// factories
	private static Factory<Body> _bodyFactory;
	private static Factory<ForceLaws> _forceLawsFactory;
	private static Factory<StateComparator> _stateComparatorFactory;

	private static void init() {
		

		ArrayList<Builder<Body>> bodyBuilders = new ArrayList<>();
		
		bodyBuilders.add(new BasicBodyBuilder());
		bodyBuilders.add(new MassLosingBodyBuilder());
		
		_bodyFactory = new BuilderBasedFactory<Body>(bodyBuilders);
		

		ArrayList<Builder<ForceLaws>> forceLawsBuilders = new ArrayList<>();
		
		forceLawsBuilders.add(new NewtonUniversalGravitionBuilder());
		forceLawsBuilders.add(new MovingTowardsFixedPointBuilder());
		forceLawsBuilders.add(new NoForceBuilder());
		
		_forceLawsFactory = new BuilderBasedFactory<ForceLaws>(forceLawsBuilders);
		
		
		
		ArrayList<Builder<StateComparator>> stateCmBuilders = new ArrayList<>();
		
		stateCmBuilders.add(new MassEqualStatesBuilder());
		stateCmBuilders.add(new EpsilonEqualStatesBuilder());
		
		_stateComparatorFactory = new BuilderBasedFactory<StateComparator>(stateCmBuilders);
	}

	private static void parseArgs(String[] args) throws Exception {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			
			parseOutFileOption(line);
			parseStepsOption(line);
			parseExpOutFile(line);
			// TODO add support of -o, -eo, and -s (define corresponding parse methods)
			
			parseDeltaTimeOption(line);
			parseForceLawsOption(line);
			parseStateComparatorOption(line);
			parseModeOption(line);
			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());
		
		// output
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file, where output is written. Default value: the standard output").build());

		// steps
		cmdLineOptions.addOption(Option.builder("s").longOpt("steps").hasArg().desc("An integer representing the number of simulation steps. Default value: " + _stepsDefaultValue + ".").build());
		
		// expoutput
		cmdLineOptions.addOption(Option.builder("eo").longOpt("expout").hasArg().desc("The expected output file. If not provided no comparison is applied").build());
		
		// delta-time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ _dtimeDefaultValue + ".")
				.build());

		// MODE
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Execution Mode. Possible values: ’batch’\n"
				+ "(Batch mode), ’gui’ (Graphical User\n"
				+ "Interface mode). Default value: ’batch’.").build());
		
		// force laws
		cmdLineOptions.addOption(Option.builder("fl").longOpt("force-laws").hasArg()
				.desc("Force laws to be used in the simulator. Possible values: "
						+ factoryPossibleValues(_forceLawsFactory) + ". Default value: '" + _forceLawsDefaultValue
						+ "'.")
				.build());

		cmdLineOptions.addOption(Option.builder("cmp").longOpt("comparator").hasArg()
				.desc("State comparator to be used when comparing states. Possible values: "
						+ factoryPossibleValues(_stateComparatorFactory) + ". Default value: '"
						+ _stateComparatorDefaultValue + "'.")
				.build());

		return cmdLineOptions;
	}

	public static String factoryPossibleValues(Factory<?> factory) {
		if (factory == null)
			return "No values found (the factory is null)";

		String s = "";

		for (JSONObject fe : factory.getInfo()) {
			if (s.length() > 0) {
				s = s + ", ";
			}
			s = s + "'" + fe.getString("type") + "' (" + fe.getString("desc") + ")";
		}

		s = s + ". You can provide the 'data' json attaching :{...} to the tag, but without spaces.";
		return s;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		/*if (_inFile == null) {
			throw new ParseException("In batch mode an input file of bodies is required");
		}*/
	}

	private static void parseOutFileOption(CommandLine line) /* throws ParseException */ {
		_outFile = line.getOptionValue("o");
		
	}
	
	private static void parseStepsOption(CommandLine line) throws ParseException {
		String steps = line.getOptionValue("steps", _stepsDefaultValue.toString());
		try {
			_steps = Integer.parseInt(steps);
			assert (_steps > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid steps value: " + steps);
		}
	}
	
	private static void parseExpOutFile(CommandLine line) {
		
		_expOutFile = line.getOptionValue("eo");
	}
	
	private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", _dtimeDefaultValue.toString());
		try {
			_dtime = Double.parseDouble(dt);
			assert (_dtime > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + dt);
		}
	}


	
	private static JSONObject parseWRTFactory(String v, Factory<?> factory) {

		// the value of v is either a tag for the type, or a tag:data where data is a
		// JSON structure corresponding to the data of that type. We split this
		// information
		// into variables 'type' and 'data'
		//
		int i = v.indexOf(":");
		String type = null;
		String data = null;
		if (i != -1) {
			type = v.substring(0, i);
			data = v.substring(i + 1);
		} else {
			type = v;
			data = "{}";
		}

		// look if the type is supported by the factory
		boolean found = false;
		for (JSONObject fe : factory.getInfo()) {
			if (type.equals(fe.getString("type"))) {
				found = true;
				break;
			}
		}

		// build a corresponding JSON for that data, if found
		JSONObject jo = null;
		if (found) {
			jo = new JSONObject();
			jo.put("type", type);
			jo.put("data", new JSONObject(data));
		}
		return jo;

	}

	private static void parseForceLawsOption(CommandLine line) throws ParseException {
		String fl = line.getOptionValue("fl", _forceLawsDefaultValue);
		_forceLawsInfo = parseWRTFactory(fl, _forceLawsFactory);
		if (_forceLawsInfo == null) {
			throw new ParseException("Invalid force laws: " + fl);
		}
	}

	private static void parseStateComparatorOption(CommandLine line) throws ParseException {
		String scmp = line.getOptionValue("cmp", _stateComparatorDefaultValue);
		_stateComparatorInfo = parseWRTFactory(scmp, _stateComparatorFactory);
		if (_stateComparatorInfo == null) {
			throw new ParseException("Invalid state comparator: " + scmp);
		}
	}

	private static void parseModeOption(CommandLine line) throws Exception {
		//ModoEjecucion batch = ModoEjecucion.BATCH;
		//ModoEjecucion gui = ModoEjecucion.GUI;

		if (line.getOptionValue("m") == null)
			startBatchMode();
		else {
			if (line.getOptionValue("m").equals("batch")) {
				startBatchMode();
			} else if (line.getOptionValue("m").equals("gui")) {
				startGuiMode();
			}
			if (!line.getOptionValue("m").equals("batch")
					&& !line.getOptionValue("m").equals("gui"))
				throw new ParseException("A mode is required");
		}

		 _mode = line.getOptionValue("m");
		if (_mode == null) {
			throw new ParseException("A mode is required");
		}
	}
	
	private static void startGuiMode() throws FileNotFoundException, InvocationTargetException, InterruptedException{
		
		ForceLaws forceLaws = _forceLawsFactory.createInstance(_forceLawsInfo);
		PhisicsSimulator phySim = new PhisicsSimulator(_dtime, forceLaws);
		Controller ctrl = new Controller1(phySim, _bodyFactory, _forceLawsFactory);
		if (Main._inFile != null) {
			InputStream is = new FileInputStream(new File(Main._inFile));
			try {
				ctrl.reset();
			ctrl.loadBodies(is);
			}catch(Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}

		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				new MainWindow(ctrl);
			}
		});
		
	}

	private static void startBatchMode() throws Exception {
		Controller ctrl;
		InputStream is = new FileInputStream(new File(_inFile));
		OutputStream os = Main._outFile == null ? 
				System.out : new FileOutputStream(new File(_outFile));
		
		ForceLaws forceLaws = _forceLawsFactory.createInstance(_forceLawsInfo);
		
		PhisicsSimulator sim = new PhisicsSimulator(_dtime, forceLaws);
		
		InputStream expOut = null;
		
		StateComparator stateCmp = null;
		
		if(_expOutFile !=null) {
			expOut =new FileInputStream(new File (_expOutFile));
			stateCmp= _stateComparatorFactory.createInstance(_stateComparatorInfo);
			ctrl = new Controller2(sim, _bodyFactory, expOut, stateCmp, _forceLawsFactory);
		}
		else {
			ctrl = new Controller1(sim, _bodyFactory, _forceLawsFactory);
		}
		
		ctrl.loadBodies(is);
		ctrl.run(_steps, os);
	}

	private static void start(String[] args) throws Exception {
		parseArgs(args);
		
	}

	public static void main(String[] args) {
		try {
			init();
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
}
