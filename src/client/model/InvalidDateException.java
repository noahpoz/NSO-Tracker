package client.model;

@SuppressWarnings("serial")
public class InvalidDateException extends Exception {
	public InvalidDateException() {
		super("The date information given is not valid.");
	}
}
