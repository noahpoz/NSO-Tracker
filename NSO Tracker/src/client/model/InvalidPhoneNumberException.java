package client.model;

public class InvalidPhoneNumberException extends Exception {
	
	public InvalidPhoneNumberException() {
		super ("Phone number format is incorrect.");
	}

}
