package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;



public class ControlPanel extends JPanel implements SimulatorObserver {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
// ...
private Controller _ctrl;
private boolean _stopped;

private JSpinner nSteps;
private JTextField time;
private JFileChooser fc;

private ForceLawsDialog _changeForceLawsDialog;

JButton openButton;
JButton forceLawsButton;
JButton exitButton;
JButton stopButton;
JButton runButton;

ControlPanel(Controller ctrl) {
	this._ctrl = ctrl;
	_stopped = true;

	this.openButton = new JButton();
	this.forceLawsButton = new JButton();
	this.exitButton = new JButton();
	this.stopButton = new JButton();
	this.runButton = new JButton();

	_changeForceLawsDialog = null;
	fc = null;
	
	initGUI();
	_ctrl.addObserver(this);
}
private void initGUI() {
	JToolBar toolBar = new JToolBar();

	//BOTON CARGAR FICHERO
	openButton.setToolTipText("Carga un fichero de cuerpos");
	openButton.setIcon(new ImageIcon("resources/icons/open.png"));
	openButton.setPreferredSize(new Dimension(36, 36));
	openButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			seleccionarFichero();
		}
	});
	
	// SELECCIONAR LEY FISICA
	forceLawsButton.setToolTipText("Selecciona la ley de fuerza");
	forceLawsButton.setIcon(new ImageIcon("resources/icons/physics.png"));
	forceLawsButton.setPreferredSize(new Dimension(36, 36));
	forceLawsButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
			seleccionarLey();
			
		}

	
	});
	
	//BOTON STOP
	  stopButton.setBounds(400, 20,150,100);
	  stopButton.setIcon(new ImageIcon("resources/icons/stop.png")); 
	  stopButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				stopButton();
			}

			private void stopButton() {
				
				_stopped = true;
			}	
		});
	  
	  //JSPINER
		this.nSteps = new JSpinner(new SpinnerNumberModel(10000, 0, 10000, 100));
		this.nSteps.setToolTipText("pasos a ejecutar: 1-10000");
		this.nSteps.setMaximumSize(new Dimension(70, 70));
		this.nSteps.setMinimumSize(new Dimension(70, 70));
	  
	  //BOTON RUN
	  runButton.setBounds(600, 20,150,100);
	  runButton.setIcon(new ImageIcon("resources/icons/run.png")); 
	  runButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				runButton();
			}
			
			private void runButton() {
			try {
					if (Double.parseDouble(time.getText()) > 0) {
						_ctrl.setDeltaTime(Double.parseDouble(time.getText()));
						int n = Integer.parseInt(nSteps.getValue().toString());
						forceLawsButton.setEnabled(false);
						openButton.setEnabled(false);
						exitButton.setEnabled(false);
						_stopped = false;
						run_sim(n);
				} else 
					JOptionPane.showMessageDialog(null, "El valor 'time' debe ser positivo");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
				
			}
		});
	  
	// BOTON TIME
			this.time = new JTextField("1000", 5);
			this.time.setToolTipText("Tiempo actual");
			this.time.setMaximumSize(new Dimension(70, 70));
			this.time.setMinimumSize(new Dimension(70, 70));
			this.time.setEditable(true);
			
			 //BOTON EXIT
			  exitButton.setBounds(600, 20,150,100);
			  exitButton.setIcon(new ImageIcon("resources/icons/exit.png")); 
			  exitButton.addActionListener(new ActionListener() { 
					public void actionPerformed(ActionEvent e) {
						exit();
					}

					
				});
	  
	  // ADDS
		toolBar.add(openButton);
		toolBar.add(forceLawsButton);
		toolBar.add(stopButton);
		toolBar.add(runButton);
		toolBar.add(new JLabel(" Steps: "));
		toolBar.add(nSteps);
		toolBar.add(new JLabel(" Time: "));
		toolBar.add(this.time);
		toolBar.add(Box.createHorizontalStrut(270));
		toolBar.add(exitButton);
		this.add(toolBar);
	
}
public void exit() {
	int n = JOptionPane.showOptionDialog(new JFrame(), "Are sure you want to quit?", "Quit",
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
	if (n == 0) {
		System.exit(0);
	}
	
}

private void seleccionarFichero() {
	
	if(fc == null) {
		this.fc = new JFileChooser();
	}
	
	int sel = this.fc.showOpenDialog(null);
	if (sel == JFileChooser.APPROVE_OPTION) {
		File fichero = this.fc.getSelectedFile();
		try {
			InputStream is = new FileInputStream(fichero);
			_ctrl.reset();
			this._ctrl.loadBodies(is);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
}
protected void seleccionarLey() {
	if(_changeForceLawsDialog == null) {
		_changeForceLawsDialog = new ForceLawsDialog((Frame) SwingUtilities.getWindowAncestor(this), _ctrl.getForceLawsInfo());
		
	}
	
	int status = _changeForceLawsDialog.open();
	
	if(status == 1) {
		try {
			JSONObject obj = _changeForceLawsDialog.get_selectedLaws();
			_ctrl.setForceLaws(obj);
		}catch(Exception e) {
			JOptionPane.showMessageDialog(this.getParent(), "Something went wrong: " + e.getLocalizedMessage() , "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
private void run_sim(int n) {
if ( n>0 && !_stopped ) {
try {
_ctrl.run(n);
} catch (Exception e) {
	JOptionPane.showMessageDialog(null, e.getMessage());
	forceLawsButton.setEnabled(false);
	openButton.setEnabled(false);
	exitButton.setEnabled(false);
	runButton.setEnabled(false);
	stopButton.setEnabled(false);
_stopped = true;
return;
}
SwingUtilities.invokeLater( new Runnable() {
@Override
public void run() {
run_sim(n-1);
}
});
} else {
_stopped = true;
openButton.setEnabled(true);
forceLawsButton.setEnabled(true);
exitButton.setEnabled(true);
// TODO enable all buttons
}
}
// SimulatorObserver methods
// ...
@Override
public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
	
	
}
@Override
public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
	this.nSteps.setValue(9000);
	this.time.setText("2500");
	
	
}
@Override
public void onBodyAdded(List<Body> bodies, Body b) {
	
	
}
@Override
public void onAdvance(List<Body> bodies, double time) {
	try {
		
	}catch(NumberFormatException e) {
		JOptionPane.showMessageDialog(this, "formato de tiempo no valido", "Error", JOptionPane.ERROR_MESSAGE);
		this.nSteps.setValue(1000);
		this.time.setText("1000");
		
	}
	
}
@Override
public void onDeltaTimeChanged(double dt) {
	this.time.setText("" + dt);
}
@Override
public void onForceLawsChanged(String fLawsDesc) {
	// TODO Auto-generated method stub
	
}
}
