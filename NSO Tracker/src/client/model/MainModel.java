package client.model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import client.view.MainView;

public class MainModel extends Observable {
	
	private MainView _view;
	private boolean _viewNotSet;
	
	private ArrayList<InfoPacket> _infoPackets;
	private String _currentEventID;
	
	private Random _numGenerator;
	
	public MainModel(MainView view) {
		_view = view;
		_viewNotSet = true;
		
		_numGenerator = new Random();
		
		_infoPackets = new ArrayList<InfoPacket>();
		
		for (int i = 0; i < 30; i++) {
			_infoPackets.add(new InfoPacket(generateRandomID()));
		}
		
	}
	
	public void ready() throws UpdateException {
		if (_viewNotSet) {
			setChanged();
			notifyObservers();
			_viewNotSet = false;
		} else {
			throw new UpdateException();
		}
	}
	
	public void saveBasicInfo(String[] fields) {
		_view.saveSuccessful(true);
	}
	
	public void saveFollowUpInfo(String[] fields) {
		_view.saveSuccessful(true);
	}
	
	public ArrayList<InfoPacket> getCurrentEventPackets() {
		return _infoPackets;
	}
	
	public void eventSelected(String id) {
		
	}
	
	public ArrayList<String[]> getEvents() {
		String[] temp1 = new String[] {"Ellicott Bowl Bash", generateRandomID()};
		String[] temp2 = new String[] {"Ice Cream Social", generateRandomID()};
		
		ArrayList<String[]> list = new ArrayList<String[]>();
		list.add(temp1);
		list.add(temp2);
		
		return list;
	}
	
	private String generateRandomID() {
		Integer num =  new Integer(_numGenerator.nextInt());
		return num.toString();
	}
	
	public String getIDFromName(String s) {
		for (InfoPacket p : _infoPackets) {
			if (p.getName().equals(s)) return p.getID(); 
		}
		return null;
	}
}
