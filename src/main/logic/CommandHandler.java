package main.logic;

import java.util.ArrayList;
import java.util.LinkedList;

import main.storage.Storage;
import main.storage.Task;


public abstract class CommandHandler {
	
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
