package client.view;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

import javax.swing.*;

import client.controller.EventList;
import client.controller.NameList;
import client.controller.SearchBar;
import client.controller.SearchBar.*;
import client.model.InfoPacket;
import client.model.MainModel;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	private static final int MAIN_WIDTH = 720;
	private static final int MAIN_HEIGHT = 450;
	private static final String TITLE = "NSO Tracker Client";
	//private static final int SEARCH_BAR_WIDTH = 250;

	private static final int MENU_HEIGHT = 40;
	private static final int MENU_PADDING = 8;
	private static final int BUTTON_WIDTH = 115;

	private static final int EVENT_SELECTOR_WIDTH = 200;

	private static final int BASIC_WIDTH = 250;
	private static final int BASIC_HEIGHT = 100;

	private MainWindow _self;
	private MainModel _model;
	private JLayeredPane _contentPane;

	private EditPane _currentEditPane;

	private ArrayList<JComponent> _components;

	public MainWindow(MainModel model) {
		super();
		_self = this;
		_model = model;
		_components = new ArrayList<JComponent>();

		setSize(MAIN_WIDTH, MAIN_HEIGHT);
		setTitle(TITLE);
		setResizable(false);
		setVisible(true);

		//we will add all the main window's components to this content pane
		_contentPane = new JLayeredPane();
		_contentPane.setLayout(null);
		setContentPane(_contentPane);

		int mainHeight = this.getRootPane().getHeight();

		//LIST OF EVENTS
		JScrollPane eventPane = new JScrollPane();
		EventList eventList = new EventList(_model, eventPane);
		eventPane.setViewportView(eventList);
		_contentPane.add(MainView.formatJComponent(eventPane, EVENT_SELECTOR_WIDTH, 
				mainHeight - MENU_PADDING * 2, MENU_PADDING, MENU_PADDING), 50, 0);

		//LIST OF PROFILES
		JScrollPane namePane = new JScrollPane();
		NameList masterList = new NameList(3, _model, namePane);
		namePane.setViewportView(masterList);
		_contentPane.add(MainView.formatJComponent(namePane, MAIN_WIDTH - MENU_PADDING * 3 - EVENT_SELECTOR_WIDTH, 
				mainHeight - MENU_HEIGHT - MENU_PADDING * 3, MENU_PADDING * 2 + EVENT_SELECTOR_WIDTH, MENU_HEIGHT + MENU_PADDING * 2));
		_components.add(namePane);

		//NEW PROFILE BUTTON
		JButton newProfileButton = new JButton("(+) New Profile");
		_contentPane.add(MainView.formatJComponent(newProfileButton, BUTTON_WIDTH, MENU_HEIGHT,
				MENU_PADDING * 2 + EVENT_SELECTOR_WIDTH, MENU_PADDING));
		_components.add(newProfileButton);

		//SEARCH BAR
		int searchBarWidth = MAIN_WIDTH - (MENU_PADDING * 5 + EVENT_SELECTOR_WIDTH + BUTTON_WIDTH * 2);
		SearchBar searchBar = new SearchBar("Search the database...");
		_contentPane.add(MainView.formatJComponent(searchBar, searchBarWidth, MENU_HEIGHT,
				MENU_PADDING * 3 + EVENT_SELECTOR_WIDTH + BUTTON_WIDTH, MENU_PADDING), 51, 0);
		updateSearchableContents(searchBar);
		searchBar.setSelectionListener(new MySelectionListener() {
			@Override
			public void elementSelected(SelectionEvent e) {
				masterList.findIDInList(_model.getIDFromName(e.getSelectedElement()));
			}
		});
		_components.add(searchBar);

		//EDIT PROFILE BUTTON
		JButton editProfileButton = new JButton("Edit Profile");
		_contentPane.add(MainView.formatJComponent(editProfileButton, BUTTON_WIDTH, MENU_HEIGHT,
				MENU_PADDING * 4 + EVENT_SELECTOR_WIDTH + searchBarWidth + BUTTON_WIDTH, MENU_PADDING));
		_components.add(editProfileButton);
	}

	public void saveSuccessful(boolean b) {
		if (_currentEditPane != null) {
			if (b) {
				_currentEditPane.setVisible(false);
				_currentEditPane = null;
				componentsAreEnabled(true);
			} else {
				//will need to implement a dialog here
				System.err.println("There was a problem saving the data.");
			}
		}
	}

	private void updateSearchableContents(SearchBar s) {
		String[] contents = new String[_model.getCurrentEventPackets().size()];
		int index = 0;
		for (InfoPacket p : _model.getCurrentEventPackets()) {
			contents[index] = p.getName();
			index++;
		}
		s.setSearchableContents(contents);
	}

	public void componentsAreEnabled(boolean b) {
		for (JComponent c : _components) {
			c.setEnabled(b);
		}
	}
}
