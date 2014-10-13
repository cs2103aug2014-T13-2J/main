package main.logic;

import java.util.ArrayList;

import main.storage.Task;

public class DisplayHandler extends CommandHandler {
	public static final String MESSAGE_EMPTY = "There is nothing to display";
	public static final String DISPLAY_SUCCESS_MESSAGE = "Display successful.";
	public static final Integer TIME_STRING_START_INDEX = 0;
	public static final Integer TIME_STRING_END_INDEX = 5;
	public static final String SPACE = " ";
	
	public DisplayHandler(String details) {
		super(details);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute() {
		ArrayList<Task> tasks = getCurrentTaskList();
		if (tasks.isEmpty()) {
			System.out.println(MESSAGE_EMPTY);
		} else {
			for (int i = 0; i < tasks.size(); i++) {
				displayTask(tasks.get(i));
			}
		}
		return DISPLAY_SUCCESS_MESSAGE;
	}

	private static void displayTask(Task task) {
		String result = "";
		getDescription(result, task);
		getVenue(result, task);
		if(task.getHasStartDate() && task.getHasEndTime()) { //append "from.. ..to"
			addFrom(result, task);
			addStartDate(result, task);
			if(task.getHasStartTime()) {
				addStartTime(result, task); 
			}
			addEndDate(result, task);
			addEndTime(result, task); //assumes that there is an endTime if there is a startTime
		} else if (task.getHasStartDate()) {
			addOn(result, task);
			addStartDate(result, task);
			if(task.getHasStartTime()) {
				addAt(result, task);
				addStartTime(result, task);
			}
		}
		
		if(task.getHasRecurrence()) {
			addRecurrence(result, task);
		}
		
		if(task.getCompleted()) {
			addCompleted(result, task);
		}
		
		System.out.println(result);
	}
	
	private static String getDescription(String result, Task task) {
		return result + task.getDescription() + SPACE;
	}
	
	private static String getVenue(String result, Task task) {
		return result + task.getVenue() + SPACE;
	}
	
	private static String addFrom(String result, Task task) {
		return result + "from" + SPACE;
	}
	
	private static String addStartDate(String result, Task task) {
		return result + task.getStartDate() + SPACE;
	}
	
	private static String addStartTime(String result, Task task) {
		return result + task.getStartTime().toString().substring(TIME_STRING_START_INDEX, TIME_STRING_END_INDEX) + SPACE;
	}
	
	private static String addEndDate(String result, Task task) {
		return result + task.getEndDate() + SPACE;
	}
	
	private static String addEndTime(String result, Task task) {
		return result + task.getEndTime().toString().substring(TIME_STRING_START_INDEX, TIME_STRING_END_INDEX) + SPACE;
	}
	
	private static String addOn(String result, Task task) {
		return result + "on" + SPACE;
	}
	
	private static String addAt(String result, Task task) {
		return result + "at" + SPACE;
	}
	
	private static String addRecurrence(String result, Task task) {
		return result + "(" + task.getRecurrence() + ")" + SPACE;
	}
	
	private static String addCompleted(String result, Task task) {
		return result + "(" + task.getCompleted() + ")" + SPACE;
	}
	
}