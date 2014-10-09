package main.logic;

import java.util.ArrayList;
import java.util.List;

import main.storage.Storage;
import main.storage.Task;

public abstract class CommandHandler {
	
	public CommandHandler(String details) {
		
	}

	public abstract String execute();
	
	protected ArrayList<Task> getCurrentTaskList() {
		ArrayList<Task> tasks = new ArrayList<Task>();
		Storage storage = new Storage(tasks);
		return tasks;
	}
}
