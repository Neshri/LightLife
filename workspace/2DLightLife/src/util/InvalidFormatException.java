package util;

public class InvalidFormatException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg;
	
	public InvalidFormatException(String error) {
		msg = error;
	}
	
	public String getMessage() {
		return msg;
	}
}
