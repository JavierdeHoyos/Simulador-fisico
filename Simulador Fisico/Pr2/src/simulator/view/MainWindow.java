package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import simulator.control.Controller;

public class MainWindow extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	
	
	public MainWindow(Controller ctrl) {
	super("Physics Simulator");
	_ctrl = ctrl;
	initGUI();
	}
	
	private void initGUI() {
		
	
ControlPanel ctrlPanel = new ControlPanel(_ctrl);
	
	//BodiesInfo bodiesInfo = new BodiesInfo(_ctrl);
	
	Viewer universeViewer = new Viewer(_ctrl);
	
	StatusBar statusBar = new StatusBar(_ctrl);
	
	BodiesTable bodiesTable = new BodiesTable(_ctrl);
		
	this.addWindowListener(new WindowListener() {

		public void windowActivated(WindowEvent arg0) {
		}

		public void windowClosed(WindowEvent arg0) {
		}

		public void windowClosing(WindowEvent arg0) {
			ctrlPanel.exit();
		}

		public void windowDeactivated(WindowEvent arg0) {
		}

		public void windowDeiconified(WindowEvent arg0) {
		}

		public void windowIconified(WindowEvent arg0) {
		}

		public void windowOpened(WindowEvent arg0) {
		}
	});
	
	JPanel mainPanel = new JPanel(new BorderLayout());
	this.add(mainPanel);
	
	JPanel contentPanel = new JPanel();
	contentPanel.setLayout(new BoxLayout(contentPanel,BoxLayout.Y_AXIS));
	mainPanel.add(contentPanel,BorderLayout.CENTER);
	
	mainPanel.add(ctrlPanel,BorderLayout.PAGE_START);
	mainPanel.add(statusBar,BorderLayout.PAGE_END);
	
	//bodiesInfo.setPreferredSize(new Dimension(800,300));
	//contentPanel.add(bodiesInfo);
	contentPanel.add(bodiesTable);
	universeViewer.setPreferredSize(new Dimension(800,600));
	contentPanel.add(new JScrollPane(universeViewer, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	
	
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    pack();
    setVisible(true);
	}
}

