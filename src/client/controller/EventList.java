package client.controller;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import client.model.MainModel;
import client.view.MainView;

@SuppressWarnings("serial")
public class EventList extends JTable implements Observer {

	MainModel _model;
	JScrollPane _superPane;
	
	private ListSelectionListener _selectionListener;
	private int _lastSelectedIndex;
	
	String[][] _currentListElements;

	public static final String TITLE = MainView.spacePadding(17) + "Events";

	public EventList(MainModel model, JScrollPane pane) {
		_model = model;
		_superPane = pane;

		_model.addObserver(this);

		setModel(new CustomTableModel(new String[][] {{}}, new String[] {TITLE}));
		getTableHeader().setPreferredSize(new Dimension(getWidth(), 30));
		getTableHeader().setFont(MainView.sansSerif(12));
		setRowHeight(20);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		_selectionListener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (getSelectedRow() > -1) { 
					_lastSelectedIndex =  getSelectedRow();
					String id = _currentListElements[getSelectedRow()][1];
					_model.eventSelected(id);
				}
			}
		};
	}

	@Override
	public void update(Observable o, Object arg) {
		_currentListElements = new String[_model.getEvents().size()][2];
		String[][] temp = new String[_model.getEvents().size()][1];
		int index = 0;
		for (String[] s : _model.getEvents()) {
			_currentListElements[index][0] = s[0];
			_currentListElements[index][1] = s[1];
			temp[index][0] = s[0];
			index++;
		}
		getSelectionModel().removeListSelectionListener(_selectionListener);
		setModel(new CustomTableModel(temp, new String[] {TITLE}));
		setRowSelectionInterval(0, _lastSelectedIndex);
		getSelectionModel().addListSelectionListener(_selectionListener);
	}

	private class CustomTableModel extends DefaultTableModel {
		public CustomTableModel(Object[][] a, Object[] b) {
			super(a, b);
		}

		@Override
		public boolean isCellEditable(int row, int column){  
			return false;  
		}
	}
}
