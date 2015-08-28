package client.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import client.controller.EditPane;
import client.controller.EventList;
import client.controller.NameList;
import client.controller.SearchBar;
import client.controller.SearchBar.*;
import client.model.InfoPacket;
import client.model.MainModel;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements Observer {

	private static final int MAIN_WIDTH = 760;
	private static final int MAIN_HEIGHT = 480;
	private static final String TITLE = "NSO Tracker Client";

	public static final int MENU_HEIGHT = 40;
	private static final int MENU_PADDING = 8;
	private static final int BUTTON_WIDTH = 135;

	private static final int EVENT_SELECTOR_WIDTH = 200;
	public static final int HEADER_WIDTH = MAIN_WIDTH - MENU_PADDING * 3 - EVENT_SELECTOR_WIDTH;

	private MainModel _model;
	private JLayeredPane _contentPane;

	private EditPane _currentEditPane;

	private ArrayList<JComponent> _components;
	SearchBar _searchBar;
	JScrollPane _namePane;
	NameList _nameList;

	public MainWindow(MainModel model) {
		super();
		_model = model;
		_components = new ArrayList<JComponent>();

		_model.addObserver(this);

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
				mainHeight - MENU_PADDING * 3 - MENU_HEIGHT, MENU_PADDING, MENU_PADDING), 50, 0);
		_components.add(eventList);

		//NEW EVENT BUTTON
		JButton newEventButton = new JButton("(+) New Event");
		_contentPane.add(MainView.formatJComponent(newEventButton, EVENT_SELECTOR_WIDTH, MENU_HEIGHT, 
				MENU_PADDING, mainHeight - MENU_HEIGHT - MENU_PADDING));
		_components.add(newEventButton);

		//PROFILE LIST HEADER
		JScrollPane headerPanel = new JScrollPane();
		NameListTableHeader tableHeader = new NameListTableHeader();
		headerPanel.setViewportView(tableHeader);
		headerPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		_contentPane.add(MainView.formatJComponent(headerPanel, HEADER_WIDTH,
				MENU_HEIGHT, MENU_PADDING * 2 + EVENT_SELECTOR_WIDTH, MENU_HEIGHT + MENU_PADDING * 2));
		_components.add(headerPanel);


		//LIST OF PROFILES
		_namePane = new JScrollPane();
		_nameList = new NameList(_model);
		_namePane.setViewportView(_nameList);
		_contentPane.add(MainView.formatJComponent(_namePane, HEADER_WIDTH, 
				mainHeight - MENU_HEIGHT * 2 - MENU_PADDING * 4 - 2, 
				MENU_PADDING * 2 + EVENT_SELECTOR_WIDTH, MENU_HEIGHT * 2 + MENU_PADDING * 3));
		_components.add(_namePane);
		_components.add(_nameList);

		//NEW PROFILE BUTTON
		JButton newProfileButton = new JButton("(+) New Profile");
		_contentPane.add(MainView.formatJComponent(newProfileButton, BUTTON_WIDTH, MENU_HEIGHT,
				MENU_PADDING * 2 + EVENT_SELECTOR_WIDTH, MENU_PADDING));
		_components.add(newProfileButton);

		//SEARCH BAR
		int searchBarWidth = MAIN_WIDTH - (MENU_PADDING * 5 + EVENT_SELECTOR_WIDTH + BUTTON_WIDTH * 2);
		_searchBar = new SearchBar("Search the database...");
		_contentPane.add(MainView.formatJComponent(_searchBar, searchBarWidth, MENU_HEIGHT,
				MENU_PADDING * 3 + EVENT_SELECTOR_WIDTH + BUTTON_WIDTH, MENU_PADDING), 51, 0);
		updateSearchableContents();
		_searchBar.setSelectionListener(new MySelectionListener() {
			@Override
			public void elementSelected(SelectionEvent e) {
				_nameList.findIDInList(_model.getIDFromName(e.getSelectedElement()));
			}
		});
		_components.add(_searchBar);

		//EDIT PROFILE BUTTON
		JButton editProfileButton = new JButton("Edit Profile");
		_contentPane.add(MainView.formatJComponent(editProfileButton, BUTTON_WIDTH, MENU_HEIGHT,
				MENU_PADDING * 4 + EVENT_SELECTOR_WIDTH + searchBarWidth + BUTTON_WIDTH, MENU_PADDING));
		_components.add(editProfileButton);
		editProfileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = _nameList.getSelectedIndex();
				if (index > -1) openEditPane(_model.getCurrentEventPackets().get(index).getProfileID());
			}
		});
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

	private void openEditPane(String profileID) {
		//determine proper arrow offset
		int scrollBarValue = _nameList.getCurrentScrollBarIndex() / NameList.CELL_HEIGHT;
		int selectedIndex = _nameList.getSelectedIndex();
		int onscreenIndex = (selectedIndex - scrollBarValue);
		if (onscreenIndex < 0 || onscreenIndex > 9) {
			_nameList.findIDInList(profileID);
			onscreenIndex = 0;
		}

		int cellOffset = onscreenIndex;
		int scaleBack = 72;
		int offsetY = MENU_PADDING * 3 + MENU_HEIGHT * 2 - scaleBack;

		if (cellOffset > 5) {
			offsetY += (cellOffset - 5) * NameList.CELL_HEIGHT;
			cellOffset = 5;
		}

		_currentEditPane = new EditPane(NameList.CELL_HEIGHT * cellOffset + 
				EditPane.TRIANGLE_HALF + scaleBack, 280, offsetY, _model);
		componentsAreEnabled(false);
		_contentPane.add(_currentEditPane, 52, 0);
	}

	private void updateSearchableContents() {
		String[] contents = new String[_model.getCurrentEventPackets().size()];
		int index = 0;
		for (InfoPacket p : _model.getCurrentEventPackets()) {
			contents[index] = p.name;
			index++;
		}
		_searchBar.setSearchableContents(contents);
	}

	public void componentsAreEnabled(boolean b) {
		for (JComponent c : _components) {
			c.setEnabled(b);
			c.setFocusable(b);
			if (b) {
				_namePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			} else {
				_namePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
			}
		}
	}

	public void eventSelected(String name) {
		setTitle("NSO Tracker Client - Event: " + name);
	}

	@Override
	public void update(Observable o, Object arg) {
		updateSearchableContents();
	}
}
