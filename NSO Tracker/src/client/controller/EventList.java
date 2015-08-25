package client.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import client.model.MainModel;
import client.view.MainView;

public class EventList extends JTable implements Observer {
	
	MainModel _model;
	JScrollPane _superPane;
	ActionListener _listener;
	
	String[][] _currentListElements;
	
	public static final String TITLE = MainView.spacePadding(18) + "Events";
	
	public EventList(MainModel model, JScrollPane pane) {
		_model = model;
		_superPane = pane;
		
		_model.addObserver(this);
		
		setModel(new DefaultTableModel(new String[][] {{}}, new String[] {TITLE}));
		getTableHeader().setPreferredSize(new Dimension(getWidth(), 30));
		getTableHeader().setFont(MainView.sansSerif(12));
		setRowHeight(20);
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
		setModel(new DefaultTableModel(temp, new String[] {TITLE}));
	}
	
	public void setActionListener(ActionListener a) {
		_listener = a;
	}

}
