package main.logic;

import main.storage.Storage;


public abstract class CommandHandler {
	
	public static String MESSAGE_SYNC_SUCCESS = "Task successfully synced.";
	public static String MESSAGE_SYNC_FAILURE = "Task failed to sync.";
	
	final static Integer DEFAULT_SECOND = 0;
	final static Integer DEFAULT_MILLISECOND = 0;
	
	public CommandHandler(String details) {
	}

	public abstract String execute();
	
	protected void saveCurrentState() {
		Storage.getInstance().updateTaskHistory();
		Storage.writeToFile();
	}
}
