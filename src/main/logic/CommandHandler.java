package main.logic;

import java.util.ArrayList;

import main.storage.Storage;
import main.storage.Task;

public abstract class CommandHandler {
	
	final static Integer DEFAULT_SECOND = 0;
	final static Integer DEFAULT_MILLISECOND = 0;
	
	private static ArrayList<Task> tasks = new ArrayList<Task>();
	protected static Storage storage = Storage.getInstance();
	
	public CommandHandler(String details) {
	}

	public abstract String execute();
	
	protected ArrayList<Task> getCurrentTaskList() {
		tasks = storage.getTasks();
		return tasks;
	}
}
