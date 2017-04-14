package client.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.event.ChangeListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import client.model.InfoPacket;
import client.model.MainModel;
import client.view.MainView;

@SuppressWarnings("serial")
public class NameList extends JList<String> implements Observer {

	private MainModel _model;
	private NameList _self;
	
	private String _currentlySelectedID;
	private int	_currentScrollBarIndex;
	
	public static final int CELL_HEIGHT = 34;

	public NameList(MainModel model) {
		super();
		_model = model;
		_self = this;
		_model.addObserver(this);

		setLayoutOrientation(JList.VERTICAL);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setCellRenderer(new NameListRenderer());
		setFixedCellHeight(CELL_HEIGHT);

		getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (getSelectedIndex() > -1) {
					_currentlySelectedID = _model.getCurrentEventPackets().get(getSelectedIndex()).getProfileID();
					System.out.println("Currently selected ID: " + _currentlySelectedID);
				}
			}
		});
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JViewport viewport = (JViewport) getParent();
				viewport.addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) {
						JScrollPane p = (JScrollPane) _self.getParent().getParent();
						_currentScrollBarIndex = p.getVerticalScrollBar().getValue();
						System.err.println(_currentScrollBarIndex);
					}
				});
			}
		});
	}

	public String getCurrentlySelectedID() {
		return _currentlySelectedID;
	}
	
	public int getCurrentScrollBarIndex() {
		return _currentScrollBarIndex;
	}

	// takes in a profile ID and selects it in the list. causes the scrollpane to jump there if necessary
	public void findIDInList(String id) {
		int currentIndex = 0;
		for (InfoPacket p : _model.getCurrentEventPackets()) {
			if (p.getProfileID().equals(id)) {
				setSelectedIndex(currentIndex);
				float ratio = ((float) currentIndex) / ((float) _model.getCurrentEventPackets().size());
				Runnable setScroll = new Runnable() {
					@Override
					public void run() {
						JScrollPane pane = (JScrollPane) getParent().getParent(); //get JViewPort, then JScrollPane
						pane.getVerticalScrollBar().setValue((int) (pane.getVerticalScrollBar().getMaximum() * ratio));
					}
				};
				SwingUtilities.invokeLater(setScroll);
				break;
			}
			currentIndex++;
		}
	}
	
	// when the info in the model has changed, we need to update the list
	@Override
	public void update(Observable o, Object arg) {
		DefaultListModel<String> tempModel = new DefaultListModel<String>();
		for (InfoPacket p : _model.getCurrentEventPackets()) {

			String element = " " + p.name
					+ MainView.spacePadding(23 - p.name.length())
					+ p.getNumberOfFollowUps()
					+ MainView.spacePadding(17)
					+ p.infoGatheredBy;
					
			tempModel.addElement(element);

		}
		setModel(tempModel);
	}

	/** Allows for the custom coloration of the list elements **/
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

			setBorder(BorderFactory.createMatteBorder(ELEMENT_SEPARATOR_THICKNESS, 0, 
					ELEMENT_SEPARATOR_THICKNESS, 0, Color.white));
			
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
