package util;

public class InvalidFormatException extends Exception{
	
	private String msg;
	
	public InvalidFormatException(String error) {
		msg = error;
	}
	
	public String getMessage() {
		return msg;
	}
}
