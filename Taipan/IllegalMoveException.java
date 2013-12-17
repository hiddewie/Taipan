package Taipan;

public class IllegalMoveException extends Exception {
	String message;
	public IllegalMoveException(String message) {
		this.message = message;
	}
	public IllegalMoveException() {
		this("");
	}
	public String getMessage() {
		return this.message;
	}
}
