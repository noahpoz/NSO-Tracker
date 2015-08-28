package client.model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import client.view.MainView;

public class MainModel extends Observable {
	
	private MainView _view;
	private boolean _viewNotSet;
	
	private ArrayList<InfoPacket> _infoPackets;
	private ArrayList<InfoPacket> _currentInfoPackets;
	private ArrayList<String[]> _events;
	
	private Random _numGenerator;
	
	public MainModel(MainView view) {
		_view = view;
		_viewNotSet = true;
		
		_numGenerator = new Random();
		
		// ****** Temporary Profile Information ******
		String ellicott = generateRandomID();
		String[] temp1 = new String[] {"Ellicott Bowl Bash", ellicott};
		String[] temp2 = new String[] {"Ice Cream Social", generateRandomID()};
		
		_events = new ArrayList<String[]>();
		_events.add(temp1);
		_events.add(temp2);
		
		_infoPackets = new ArrayList<InfoPacket>();
		_currentInfoPackets = new ArrayList<InfoPacket>();
		
		InfoPacket a = new InfoPacket(generateRandomID(), ellicott);
		a.name = "Noah Poczciwinski";
		
		InfoPacket b = new InfoPacket(generateRandomID(), ellicott);
		b.name = "Christian Koehler";
		
		_infoPackets.add(a);
		_infoPackets.add(b);
		
		// ******
		
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
	
	public void saveProfile(String profileID) {
		//insert code to save data in the cloud
		_view.saveSuccessful(true);
	}
	
	public ArrayList<InfoPacket> getCurrentEventPackets() {
		ArrayList<InfoPacket> temp = new ArrayList<InfoPacket>();
		for (InfoPacket p : _currentInfoPackets) {
			temp.add(p.deepCopy());
		}
		return temp;
	}
	
	public void eventSelected(String id) {
		//insert code for adjusting current event packets
		String eventName = "";
		for (String[] s : _events) {
			if (s[1].equals(id)) {
				eventName = s[0];
				break;
			}
		}
		_view.eventSelected(eventName);
		
		_currentInfoPackets = new ArrayList<InfoPacket>();
		for (InfoPacket p : _infoPackets) {
			if (p.getEventID().equals(id)) {
				_currentInfoPackets.add(p);
			}
		}
		
		setChanged();
		notifyObservers();
	}
	
	public ArrayList<String[]> getEvents() {
		return _events;
	}
	
	private String generateRandomID() {
		Integer num =  new Integer(_numGenerator.nextInt());
		return num.toString();
	}
	
	public String getIDFromName(String s) {
		for (InfoPacket p : _infoPackets) {
			if (p.name.equals(s)) return p.getProfileID(); 
		}
		return null;
	}
}
