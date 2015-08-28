package client.model;

import java.util.ArrayList;

/** 
 * Author: Noah Poczciwinski
 * last modified: 8/28/2015
 * 
 * Encapsulates the information it contains to prevent it from being
 * corrupted. Returns only deep copies or immutable strings to mitigate inadvertent
 * mutation. 
 * 
 * Validates incoming information to ensure it is sensible and in the proper format,
 * otherwise an exception is thrown.
 * 
 * When transmitting InfoPackets for read-only purposes, it is encouraged to send
 * a deep copy. A method to provide a deep copy is included.
 * 
 */
public class InfoPacket {
	
	public String name;
	public String infoGatheredBy;
	public String additionalInfo;
	
	// {month, date, year}
	private int[] _dateGathered;

	// must be a valid phone number
	private String _phoneNumber;
	
	// perhaps I will add email validation functionality
	private String _email;

	// String[] = { TBD }
	// profiles can only be added programmatically, not deleted
	private ArrayList<String[]> _followUps;

	// unique string of ints to identify individual profile
	// must be immutable by outside objects
	private String _profileID;

	// unique string of ints to identify event to which profile belongs
	// must be immutable by outside objects
	private String _eventID;

	public InfoPacket(String profileID, String eventID) {
		_profileID = profileID;
		_eventID = eventID;
		
		name = "";
		infoGatheredBy = "Nate Schutt";
		additionalInfo = "";
		
		_phoneNumber = "";
		_email = "";
		_dateGathered = new int[3];
		_followUps = new ArrayList<String[]>();
	}

	// ***** Accessor Methods *****
	public String getPhoneNumber() { return _phoneNumber; }
	public String getEmail() { return _email; }
	public String getProfileID() { return _profileID; }
	public String getEventID() { return _eventID;}

	/** Returns a deep copy of the ArrayList containing follow up information (ArrayList and contents are copied). **/
	public ArrayList<String[]>	getFollowUps() {
		ArrayList<String[]> deepCopy = new ArrayList<String[]>();
		for (String[] s : _followUps) {
			String[] copy = new String[s.length];
			for (int i = 0; i < s.length; i++) {
				copy[i] = s[i];
			}
			deepCopy.add(copy);
		}
		return deepCopy;
	}

	/** Returns a shallow copy (is not deep because contents are primitives) of the int[] containing the dates. <br>
	 *  {0 - 11 (Month), 0 - 30 (Date), 0 - 1 (2015 or 2016)}
	 **/
	public int[] getDateGathered() {
		int[] copy = new int[_dateGathered.length];
		for (int i = 0; i < _dateGathered.length; i++) {
			copy[i] = _dateGathered[i];
		}
		return copy;
	}
	
	public int getNumberOfFollowUps() {
		return _followUps.size();
	}

	// ***** Mutator Methods *****

	/** Month: 0-11 <br>
	 *  Date:  0-30 <br>
	 *  Year:  0-1  <br>
	 */
	public void setDateGathered(int month, int date, int year) throws InvalidDateException {
		if (!validateDate(month, date, year)) throw new InvalidDateException();
		_dateGathered[0] = month;
		_dateGathered[1] = date;
		_dateGathered[2] = year;
	}

	public void setPhoneNumber(String number) throws InvalidPhoneNumberException {

		boolean stillGood = true;
		char[] chars = number.toCharArray();

		if (chars.length == 14) {
			if (chars[0]  != '(')  stillGood = false;
			if (!isInt(chars[1]))  stillGood = false;
			if (!isInt(chars[2]))  stillGood = false;
			if (!isInt(chars[3]))  stillGood = false;
			if (chars[4]  != ' ')  stillGood = false;
			if (!isInt(chars[5]))  stillGood = false;
			if (!isInt(chars[6]))  stillGood = false;
			if (!isInt(chars[7]))  stillGood = false;
			if (chars[8]  != '-')  stillGood = false;
			if (!isInt(chars[9]))  stillGood = false;
			if (!isInt(chars[10])) stillGood = false;
			if (!isInt(chars[11])) stillGood = false;
			if (!isInt(chars[12])) stillGood = false;
		} else {
			stillGood = false;
		}
		
		if (stillGood || number.equals("")) {
			_phoneNumber = number;
		} else {
			throw new InvalidPhoneNumberException();
		}
	}
	
	public void addFollowUp(String followedUpBy, int month, int date, int year, String notes)
		throws InvalidDateException {
		
		if (!validateDate(month, date, year)) throw new InvalidDateException();
		
		String[] followUp = new String[5];
		followUp[0] = followedUpBy;
		followUp[1] = (new Integer(month)).toString();
		followUp[2] = (new Integer(date)).toString();
		followUp[3] = (new Integer(year)).toString();
		followUp[4] = notes;
		
		_followUps.add(followUp);
	}
	
	public void setEmail(String email) {
		_email = email;
	}
	
	// ***** Helper Methods *****
	private boolean isInt(char c) {
		int num = c - '0';
		return (num >= 0 || num <= 9);
	}
	
	private boolean validateDate(int month, int date, int year) {
		if (month < 0 || month > 11) {
			return false;
		} 

		if (date < 0 || date > 30) {
			return false;
		} else {
			if ((month == 8 || month == 3 || month == 5 || month == 10) && date > 29) {
				return false;
			} else if (month == 1 && date > 27) {
				return false;
			} 
		}

		if (year < -1 || year > 1) {
			return false;
		}
		
		return true;
	}
	
	public InfoPacket deepCopy() {
		InfoPacket copy = new InfoPacket(_profileID, _eventID);
		
		copy.name = name;
		copy.infoGatheredBy = infoGatheredBy;
		copy.additionalInfo = additionalInfo;
		
		int[] date = getDateGathered();
		try {
			copy.setDateGathered(date[0], date[1], date[2]);
		} catch (InvalidDateException e) {
			System.out.println(e.toString());
		}
		
		try {
			copy.setPhoneNumber(getPhoneNumber());
		} catch (InvalidPhoneNumberException e) {
			System.out.println(e.toString());
		}
		
		copy.setEmail(getEmail());
		
		for (String[] s : _followUps) {
			try {
				copy.addFollowUp(s[0], Integer.parseInt(s[1]), Integer.parseInt(s[2]), Integer.parseInt(s[3]), s[4]);
			} catch (NumberFormatException | InvalidDateException e) {
				System.out.println(e.toString());
			}
		}
		
		return copy;
	}
}
