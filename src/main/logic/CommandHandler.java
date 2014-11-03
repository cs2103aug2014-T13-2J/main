package main.logic;

import main.storage.Storage;


public abstract class CommandHandler {
	
	final static Integer DEFAULT_SECOND = 0;
	final static Integer DEFAULT_MILLISECOND = 0;
	
	public CommandHandler(String details) {
	}

	public abstract String execute();
	
}
