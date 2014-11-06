package main.logic;

public abstract class CommandHandler {
	
	protected final static String MESSAGE_INVALID_DATE_VALUE = "That's not a valid date.";
	protected final static String MESSAGE_INVALID_TIME_VALUE = "That's not a valid time.";
	protected final static Integer DEFAULT_SECOND = 0;
	protected final static Integer DEFAULT_MILLISECOND = 0;
	
	public CommandHandler(String details) {
	}

	public abstract String execute();
	
}
