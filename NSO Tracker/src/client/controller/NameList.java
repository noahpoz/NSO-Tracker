package client.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import client.model.InfoPacket;
import client.model.MainModel;

@SuppressWarnings("serial")
public class NameList extends JList<String> implements Observer {
	
	private int _scrollingIncrement;
	private MainModel _model;
	private JScrollPane _superPane;
	
	private static final int LIST_BORDER_THICKNESS = 2;
	
	private String _currentlySelectedID;
	
	public NameList(int inc, MainModel model, JScrollPane pane) {
		super();
		_scrollingIncrement = inc;
		_model = model;
		_model.addObserver(this);
		_superPane = pane;
		
		setLayoutOrientation(JList.VERTICAL);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setCellRenderer(new NameListRenderer());
		setFixedCellHeight(35);
		
		getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				_currentlySelectedID = _model.getCurrentEventPackets().get(getSelectedIndex()).getID();
				System.out.println("Currently selected ID: " + _currentlySelectedID);
			}
		});
	}
	
	public String getCurrentlySelectedID() {
		return _currentlySelectedID;
	}
	
	public void findIDInList(String id) {
		int currentIndex = 0;
		for (InfoPacket p : _model.getCurrentEventPackets()) {
			if (p.getID().equals(id)) {
				setSelectedIndex(currentIndex);
				float ratio = ((float) currentIndex) / ((float) _model.getCurrentEventPackets().size());
				Runnable setScroll = new Runnable() {
					@Override
					public void run() {
						_superPane.getVerticalScrollBar()
						.setValue((int) (_superPane.getVerticalScrollBar().getMaximum() * ratio));
					}
				};
				SwingUtilities.invokeLater(setScroll);
				break;
			}
			currentIndex++;
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		DefaultListModel<String> tempModel = new DefaultListModel<String>();
		for (InfoPacket p : _model.getCurrentEventPackets()) {
			
			//In here will go the code for the actual list entry
			String element = " " + p.getName();
			tempModel.addElement(element);
			
		}
		setModel(tempModel);
	}
	
	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return _scrollingIncrement;
	}
	
	private class NameListRenderer extends JLabel implements ListCellRenderer<Object> {
		
		private static final int ELEMENT_SEPARATOR_THICKNESS = 1;
		
		public NameListRenderer() {
			setOpaque(true);
		}
		
		@Override
		public Component getListCellRendererComponent(
				JList<? extends Object> list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			setText(value.toString());
			
			Color background;
			Color foreground;
			
			if (index > 0) {
				setBorder(BorderFactory.createMatteBorder(ELEMENT_SEPARATOR_THICKNESS, 0, 
						ELEMENT_SEPARATOR_THICKNESS, 0, Color.white));
			} else 
				setBorder(BorderFactory.createMatteBorder(0, 0, ELEMENT_SEPARATOR_THICKNESS, 0, Color.white));
			
			if (isSelected) {
				background = Color.blue;
				foreground = Color.white;
			} else {	//here we determine the color based on the number of follow ups
				int numFollowups = 0;
				if (index < _model.getCurrentEventPackets().size()) {
					numFollowups = _model.getCurrentEventPackets().get(index).getNumberOfFollowUps();
				}
				if (numFollowups == 0) {
					background = new Color(240, 0, 20);
					foreground = Color.white;
				} else if (numFollowups == 1) {
					background = new Color(255,140,0);
					foreground = Color.black;
				} else if (numFollowups == 2) {
					background = Color.yellow;
					foreground = Color.black;
				} else {
					background = Color.white;
					foreground = Color.black;
				}
			}
			
			setBackground(background);
			setForeground(foreground);
			setFont(new Font("Monospaced", Font.PLAIN, 15));
			
			return this;
		}
	}
}
