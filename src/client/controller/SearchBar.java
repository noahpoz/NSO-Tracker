package client.controller;

import java.awt.Graphics;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import client.controller.GhostTextField.ChangeListener;
import client.model.MainModel;
import client.view.MainView;


/** Takes in a String[] and will display narrowed-down results as the user types **/
@SuppressWarnings("serial")
public class SearchBar extends JPanel{

	private SearchBar _self;

	private int _desiredWidth;
	private int _desiredHeight;

	private GhostTextField _textField;
	private ResultsPane _resultsPane;
	private int _resultsHeight;
	private String[] _searchableContents = {};

	private MySelectionListener _listener;

	public SearchBar(String emptyText) {
		
		_self = this;
		
		_resultsHeight = 150; //default height for results pane

		setLayout(null); //we will manually arrange all the components of this window 

		_textField = new GhostTextField(emptyText);
		add(_textField);

		_resultsPane = (ResultsPane) MainView.formatJComponent(new ResultsPane(), getWidth(), _resultsHeight, 0, getHeight());
		add(_resultsPane);

		_textField.addChangeListener(new ChangeListener() {
			@Override public void textChanged() {
				if (_textField.getContents().length() > 0) {
					_resultsPane.textChanged(_textField.getContents());
					_resultsPane.setVisible(true);
				} else {
					_resultsPane.setVisible(false);
				}
				
				setSize(_desiredWidth, _desiredHeight);
			}
		});
	}

	public void setSearchableContents(String[] contents) {
		_searchableContents = contents;
		_resultsPane.searchableContentsUpdated();
	}

	public void setSelectionListener(MySelectionListener selectionListener) {
		_listener = selectionListener;
	}

	@Override
	public void setSize(int width, int height) {
		_desiredWidth = width;
		_desiredHeight = height;

		if (_resultsPane.isVisible()) {
			super.setSize(_desiredWidth, _desiredHeight + _resultsHeight);
		} else {
			super.setSize(_desiredWidth, _desiredHeight);
		}
		
		_textField.setSize(_desiredWidth, _desiredHeight);
		_resultsPane.setSize(_desiredWidth, _resultsHeight);
		_resultsPane.setLocation(0, _desiredHeight);
	}
	
	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		_textField.setEnabled(b);
	}
	
	/** definition for the pop up results box */
	private class ResultsPane extends JScrollPane {

		public JList<String> _list;

		public ResultsPane() {
			super();

			_list = new JList<String>();
			setViewportView(_list);

			_list.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					if (_listener != null && !_list.getSelectedValue().equals("No results.")) {
						_listener.elementSelected(new SelectionEvent(_list.getSelectedValue()));
						_textField.setContents("");
						setVisible(false);
					} else if (_list.getSelectedValue().equals("No results.")) {
						_textField.setContents("");
						setVisible(false);
					}
				}
			});

			setVisible(false);
		}

		public void textChanged(String text) {
			DefaultListModel<String> tempModel = new DefaultListModel<String>();
			ArrayList<String> results = createSearchResults(text);
			for (String s : results) {
				tempModel.addElement(s);
			}

			if (text.length() > 0 && results.size() == 0) {
				tempModel.addElement("No results.");
			}

			_list.setModel(tempModel);
		}

		public ArrayList<String> createSearchResults(String input) {
			ArrayList<String> mutableResults = new ArrayList<String>();
			for (int i = 0; i < _searchableContents.length; i++) {
				String entry = _searchableContents[i];
				if (entry.toLowerCase().contains((CharSequence) input.toLowerCase())) {
					mutableResults.add(entry);
				}
			}
			return mutableResults;
		}

		//if anything changed in the list of InfoPackets, we will have to adjust the results according to the new contents
		public void searchableContentsUpdated() {

		}
	}

	public interface MySelectionListener {
		public void elementSelected(SelectionEvent e);
	}

	public class SelectionEvent {

		private String _element;

		public SelectionEvent(String element) {
			_element = element;
		}

		public String getSelectedElement() {
			return _element;
		}
	}
}
