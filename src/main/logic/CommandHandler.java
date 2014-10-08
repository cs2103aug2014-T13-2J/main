package main.logic;

import java.util.List;

import main.storage.Task;

public abstract class CommandHandler {
	
	public CommandHandler(String details) {
		
	}

	public abstract String execute();
	
	protected List<Task> getCurrentTaskList() {
		return null;
	}
}
