package client.model;

import java.util.ArrayList;
import java.util.Random;

/** This is the standard procedure for packaging and transmitting profile information. It must not change. **/
public class InfoPacket {
	
	// {NAME, MONTH, DATE, YEAR, EMAIL, PHONE, ADDITIONAL, GATHERED BY}
	private String[] _basicInfo = {"Noah", "0", "0", "0", "noahpocz@buffalo.edu", "17162201008", "INFO", "Nate"};
	
	// Mutable collection of String[]'s
	// String[] = { TBD }
	private ArrayList<String[]> _followUps;
	
	//unique string of ints to identify individual profile
	private String _id;
	
	public InfoPacket(String id) {
		_id = id;
	}
	
	public String[] 	getBasicInfo() 				{ return _basicInfo;    }
	public String 		getName() 					{ return _basicInfo[0]; }
	public int 			getNumberOfFollowUps() 		{ return 1;      		}
	
	public String 		getID() 					{ return _id; 			}
	
}
