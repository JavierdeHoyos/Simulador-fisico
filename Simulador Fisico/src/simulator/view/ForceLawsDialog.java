package simulator.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.json.JSONObject;

//import org.json.JSONObject;

public class ForceLawsDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

	private int _status;
	
	private List<JSONObject> _forceLawsInfo;
	
	private JComboBox<String> cb;
	
	private int _selectedLawsIndex;
	
	private JTable _dataTable;
	private LawsTableModel _dataTableModel;
	//*********************************************************************************************************
	private class LawsTableModel extends AbstractTableModel{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int initRows = 2;
		private int currRows;
		private String[] _header = { "Key", "Value", "Description" };
		String[][] _data;

		LawsTableModel() {
			_data = new String[initRows][3];
			currRows = 0;
		
		}
		public void clear() {
			for (int i = 0; i < initRows; i++)
				for (int j = 0; j < 3; j++)
					_data[i][j] = "";
			fireTableStructureChanged();
		}

		@Override
		public String getColumnName(int column) {
			return _header[column];
		}

		@Override
		public int getRowCount() {
			return currRows;
		}

		@Override
		public int getColumnCount() {
			return _header.length;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if(columnIndex == 1)
				return true;
			
			return false;
		}

		@Override
		public String getValueAt(int rowIndex, int columnIndex) {
			return _data[rowIndex][columnIndex];
		}
		
		@Override
		public void setValueAt(Object o, int rowIndex, int columnIndex) {
			
			_data[rowIndex][columnIndex] = o.toString();
		}
		
		public String getData() {
			StringBuilder s = new StringBuilder();
			s.append('{');
			for (int i = 0; i < _data.length; i++) {
				if (!_data[i][0].isEmpty() && !_data[i][1].isEmpty()) {
					s.append('"');
					s.append(_data[i][0]);
					s.append('"');
					s.append(':');
					s.append(_data[i][1]);
					s.append(',');
				}
			}

			if (s.length() > 1)
				s.deleteCharAt(s.length() - 1);
			s.append('}');

			return s.toString();
		}
		void setRowCount(int row) {
			currRows = row;
		}
	}
		
	
	
	
	

//******************************************************************************************

public ForceLawsDialog(Frame frame, List<JSONObject> _forceLawsInfo) {
	super(frame, true);
	this.setTitle("Gravity Law Selector");
	this._forceLawsInfo = _forceLawsInfo;
	_dataTableModel = new LawsTableModel();
	//this._status=-1;
	
	 _dataTable = new JTable(_dataTableModel) {
			private static final long serialVersionUID = 1L;
			
			// we override prepareRenderer to resized rows to fit to content
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				tableColumn.setPreferredWidth(
						Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
				return component;
			}
		};
	initGui();
}

void initGui() {

	//CREACION DE PANELES
	JPanel mainPanel = new JPanel(new BorderLayout());
	
	JPanel comboBoxPanel = new JPanel(new FlowLayout());
	JPanel panelButton = new JPanel(new FlowLayout());
	JPanel centerPanel = new JPanel();
	JPanel supPanel = new JPanel(new FlowLayout());
	JPanel infPanel = new JPanel(new BorderLayout());
	 
	centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
	mainPanel.add(centerPanel,BorderLayout.CENTER);
	
	//TEXTO INFORMACION
	 String texto = "Select a force law provide values for the parametes in the Value column (default values are used dor parametes with no value)";
	 
	 JLabel info = new JLabel(texto);
	 
	 supPanel.add(info);
	 
	 //TABLA
	 
	 
	 JScrollPane table = new JScrollPane(_dataTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane. HORIZONTAL_SCROLLBAR_AS_NEEDED);
	 
	centerPanel.add(table);
	
	//COMBOBOX
	String[] forceLaws = new String[_forceLawsInfo.size()];
	JSONObject[] opt = new JSONObject[_forceLawsInfo.size()];
	
	for (int i = 0; i < _forceLawsInfo.size(); i++) {
		forceLaws[i] = _forceLawsInfo.get(i).getString("desc");
		opt[i] = _forceLawsInfo.get(i);
	}
	
	cb = new JComboBox<String>(forceLaws);
	
	cb.addActionListener(new ActionListener() {
		   @Override
		   public void actionPerformed(ActionEvent e) {
			   
			   _selectedLawsIndex =cb.getSelectedIndex();
			   
			   _dataTableModel.clear();
			  
			   JSONObject selectedForce = opt[_selectedLawsIndex].getJSONObject("data");
			   
			  ArrayList<String>s = new ArrayList <String>(selectedForce.keySet());
			  
			  _dataTableModel.setRowCount(s.size());
			   
			   for( int i = 0;i < s.size();i++ ) {
				   _dataTableModel.setValueAt(s.get(i), i, 0);
				   _dataTableModel.setValueAt(selectedForce.getString(s.get(i)), i, 2);
			   } 
		   }
		   
		});
	
	comboBoxPanel.add(new JLabel("Force Law:"));
	comboBoxPanel.add(cb, BorderLayout.CENTER);
	infPanel.add(comboBoxPanel, BorderLayout.CENTER);
	 
	//BOTONES
	JButton cancel = new JButton("cancel");
	
	 cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_status = 0;
				ForceLawsDialog.this.setVisible(false);
			}
		});
	
	 JButton ok = new JButton("ok");
		
	 ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_status = 1;
				ForceLawsDialog.this.setVisible(false);
			}
		});
	 
	 panelButton.add(ok, BorderLayout.PAGE_END);//añadir boton ok
	 panelButton.add(cancel, BorderLayout.PAGE_END);//añadir boton cancel
	 infPanel.add(panelButton, BorderLayout.PAGE_END);
	 
	//AÑADIR PANELS
	 mainPanel.add(supPanel, BorderLayout.PAGE_START);
	 mainPanel.add(centerPanel, BorderLayout.CENTER);
	 mainPanel.add(infPanel, BorderLayout.PAGE_END);
	 
	 this.add(mainPanel);
	 this.pack();
}

public int open() {
	_status = 0;
	this.setVisible(true);
	return _status;
}

public JSONObject get_selectedLaws() {
	
	JSONObject v = new JSONObject(_forceLawsInfo.get(_selectedLawsIndex));
	JSONObject a = new JSONObject( _dataTableModel.getData());
	v.put("data", a);
	v.put("type", _forceLawsInfo.get(_selectedLawsIndex).get("type"));
return v;
}


}
