package main.logic;

import java.util.ArrayList;

import main.storage.Task;

public class DeleteHandler extends CommandHandler {
	
	private String details;

	public DeleteHandler(String details) {
		super(details);
		details = this.details;
	}

	@Override
	public String execute() {
		ArrayList<Task> tasks = getCurrentTaskList();
		int userIndex = Integer.valueOf(details);
		int arrayListIndex = userIndex + 1;
		tasks.remove(arrayListIndex);
		String message = "Task " + userIndex + " has been deleted.";
		return message;
	}

}
