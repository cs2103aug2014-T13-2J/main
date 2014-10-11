package main.logic;

import java.util.ArrayList;

import main.storage.Storage;
import main.storage.Task;

public abstract class CommandHandler {
	
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
