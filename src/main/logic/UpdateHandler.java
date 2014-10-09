package main.logic;

import java.util.ArrayList;

import org.joda.time.DateTime;

import main.storage.Task;

public class UpdateHandler extends CommandHandler {
	
	private String details;

	public UpdateHandler(String details) {
		super(details);
		details = this.details;
	}

	@Override
	public String execute() {
		int index = getUserIndex(details) - 1;
		ArrayList<Task> tasks = getCurrentTaskList();
		Task currentTask = tasks.get(index);
		String taskParameter = getSecondWord(details);
		switch (taskParameter.toLowerCase()) {
		case "time":
			String newTime = getThirdWord(details);
			DateTime newDateTime;
			currentTask.setStartDateTime(newDateTime);
			String message = "Time has been changed to " + newTime; 
			return message;
		default:
			return null;	
		}
	}
	
	private int getUserIndex(String details) {
		int userIndex = Integer.valueOf(details.trim().split(" ")[0]);
		return userIndex;
	}
	
	private String getSecondWord(String details) {
		String secondWord = details.trim().split(" ")[1];
		return secondWord;
	}
	
	private String getThirdWord(String details) {
		String thirdWord = details.trim().split(" ")[2];
		return thirdWord;
	}

}
