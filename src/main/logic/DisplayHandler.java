package main.logic;

import java.io.IOException;
import java.util.ArrayList;

import jline.ConsoleReader;
import main.storage.Storage;
import main.storage.Task;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

public class DisplayHandler extends CommandHandler {

	public static final String DISPLAY_NUM_OF_TASKS = "Total number of tasks: %d\n";
	//public static final String DISPLAY_TABLE_ROW_STRING_FORMAT = "%-10s %-35s %-30s %-25s %-20s\n";
	public static final String DISPLAY_TABLE_ROW_STRING_FORMAT_EXTRA = "%-3s %-35s %-30s %-15s %-10s\n";
	public static final String MESSAGE_EMPTY = "There is nothing to display";
	public static final String MESSAGE_DISPLAY_SUCCESS = "Display successful.";
	public static final Integer TIME_STRING_START_INDEX = 0;
	public static final Integer TIME_STRING_END_INDEX = 5;
	public static final String STRING_SPACE = " ";
	public static final String MESSAGE_ADDED = " added!";

	private static String result;
	private static Storage storage = Storage.getInstance();
	static ConsoleReader console;
	
	public DisplayHandler(String details) {
		super(details);
	}

	@Override
	public String execute() {
		ArrayList<Task> tasks = storage.getTasks();
		result = "";
		String DISPLAY_TABLE_ROW_STRING_FORMAT = displayFormat();
		if (tasks.isEmpty()) {
			System.out.println(MESSAGE_EMPTY);
		} else {
			int numberOfTasks = tasks.size();
			result = String.format(DISPLAY_NUM_OF_TASKS, numberOfTasks);

			result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
					ansi().fg(RED).a("ID").reset(),
					ansi().fg(MAGENTA).a(" DESCRIPTION").reset(), ansi()
							.fg(CYAN).a(" VENUE").reset(),
					ansi().fg(YELLOW).a(" TIME").reset(),
					ansi().fg(GREEN).a(" DATE").reset());
			result += displayLineSeparator();
			for (int j = 0; j < tasks.size(); j++) {
				result += displayTaskInTable(j, tasks.get(j));
			}
			result += displayLineSeparator();
			result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
					ansi().fg(RED).a("ID").reset(),
					ansi().fg(MAGENTA).a(" DESCRIPTION").reset(), ansi()
							.fg(CYAN).a(" VENUE").reset(),
					ansi().fg(YELLOW).a(" TIME").reset(),
					ansi().fg(GREEN).a(" DATE").reset());
		}
		return result;
	}

	private static String displayLineSeparator() {
		String lineString = "";
		int terminalWidth = getTerminalWidth();
		for (int i = 0; i < terminalWidth; i++) {
			lineString += "-";
		}
		lineString += "\n";
		return lineString;
	}
	
	
	private static String displayFormat() {
		String displayFormat = "";
		int terminalWidth = getTerminalWidth();
		int id = terminalWidth/10;
		int description = terminalWidth/10*5;
		int venue = terminalWidth/10*3;
		int time = terminalWidth/10*2;
		int date = terminalWidth/10*2;
		String ID = "%-"+id+"s";
		String DESCRIPTION = "%-"+description+"s";
		String VENUE = "%-"+venue+"s";
		String TIME = "%-"+time+"s";
		String DATE = "%-"+date+"s";
		displayFormat = ID+""+DESCRIPTION+""+VENUE+""+TIME+""+DATE+"\n";
		return displayFormat;
	}

	
	private static int getTerminalWidth() {
		try {
			console = new ConsoleReader();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int terminalWidth = console.getTermwidth();
		return terminalWidth;
	}
	
	public static String displayTaskInTable(int number, Task task) {
		String DISPLAY_TABLE_ROW_STRING_FORMAT = displayFormat();
		result = "";
		String taskNumber = getTaskNumber(number);
		String taskDescription = task.getDescription();
		String taskDescriptionExtra = "";
		String taskDescriptionExtraOne = "";
		String taskDescriptionExtraTwo = "";
		//Queue<String> queueDescription = new LinkedList<String>();
		//double numLines = Math.ceil(taskDescription.length()/descriptionWidth);
		
		if(taskDescription.length() >= 25 && taskDescription.length() < 50) {
			taskDescriptionExtraOne = taskDescription.substring(25);
			taskDescription = taskDescription.substring(0,25);
		}
		
		else if(taskDescription.length() >= 50 && taskDescription.length() < 75) {
			taskDescriptionExtraTwo = taskDescription.substring(50);
			taskDescriptionExtraOne = taskDescription.substring(25,50);
			taskDescription = taskDescription.substring(0,25);
		}
		
		else if(taskDescription.length()>=75){
			taskDescriptionExtra = taskDescription.substring(75);
			taskDescriptionExtraTwo = taskDescription.substring(50,75);
			taskDescriptionExtraOne = taskDescription.substring(25,50);
			taskDescription = taskDescription.substring(0,25);
		}
		
		
		String taskVenue = "-";
		String taskVenueExtra = "";
		String taskVenueExtraOne = "";
		String taskVenueExtraTwo = "";
		if (task.getHasVenue()) {
			taskVenue = task.getVenue();
			if (taskVenue.length() >= 12 && taskVenue.length() <24) {
				taskVenueExtraOne = taskVenue.substring(12);
				taskVenue = taskVenue.substring(0, 12);
			}
			else if(taskVenue.length() >=24 && taskVenue.length() <36) {
				taskVenueExtraTwo = taskVenue.substring(24);
				taskVenueExtraOne = taskVenue.substring(12,24);
				taskVenue = taskVenue.substring(0,12);
			}
			else if(taskVenue.length()>=36){
				taskVenueExtra = taskVenue.substring(36);
				taskVenueExtraTwo = taskVenue.substring(24,36);
				taskVenueExtraOne = taskVenue.substring(12,24);
				taskVenue = taskVenue.substring(0,12);
			}
		}

		String startTaskTime = "-";
		String endTaskTime = "-";
		if (task.getHasStartTime()) {
			startTaskTime = addStartTime(task);
			if (!task.getEndTime().equals(task.getStartTime())) {
				//taskTime += "to " + addEndTime(task);
				endTaskTime = addEndTime(task);
			}
		}

		String startTaskDate = "-";
		String endTaskDate = "-";
		if (task.getHasStartDate()) {
			startTaskDate = addStartDate(task);
			if (!task.getEndDate().equals(task.getStartDate())) {
				//taskDate += "to " + addEndDate(task);
				endTaskDate = addEndDate(task);
			}
		}
		
		String nullSpace = "";
		int nullSpaceLength = taskNumber.length();
		for (int i = 0; i < nullSpaceLength; i++) {
			nullSpace += " ";
		}
		
		String to = "to";
		result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED)
				.a(taskNumber).reset(), ansi().fg(MAGENTA).a(taskDescription)
				.reset(), ansi().fg(CYAN).a(taskVenue).reset(), ansi().fg(YELLOW)
				.a(startTaskTime).reset(), ansi().fg(GREEN).a(startTaskDate).reset());		

		result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT,ansi().fg(RED).a(nullSpace).reset(),ansi()
				.fg(MAGENTA).a(taskDescriptionExtraOne).reset(),
				ansi().fg(CYAN).a(taskVenueExtraOne).reset(),ansi().fg(YELLOW).a(to).reset(), ansi().fg(GREEN).a(to).reset());
		
		
		result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT,ansi().fg(RED).a(nullSpace).reset(),ansi()
				.fg(MAGENTA).a(taskDescriptionExtraTwo).reset(),
				ansi().fg(CYAN).a(taskVenueExtraTwo).reset(),ansi().fg(YELLOW).a(endTaskTime).reset(), ansi().fg(GREEN).a(endTaskDate).reset());
		
		if (!taskDescriptionExtra.isEmpty() || !taskVenueExtra.isEmpty()) {
			double descriptionLines = Math.ceil(taskDescriptionExtra.length()/25.0);
			double venueLines = Math.ceil(taskVenueExtra.length()/12.0);
			
			System.out.println(venueLines);
			System.out.println(descriptionLines);
			
			if(venueLines == descriptionLines){
				for(int i=0;i<venueLines-1;i++){
					String displayVenue = taskVenueExtra.substring(0,12);
					String displayDescription = taskDescriptionExtra.substring(0,25);
					taskVenueExtra = taskVenueExtra.substring(12);
					taskDescriptionExtra = taskDescriptionExtra.substring(25);
					
					result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED).a(nullSpace).reset(), ansi()
							.fg(MAGENTA).a(displayDescription).reset(),
							ansi().fg(CYAN).a(displayVenue).reset(), "", "");
				}
				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED).a(nullSpace).reset(), ansi()
						.fg(MAGENTA).a(taskDescriptionExtra).reset(),
						ansi().fg(CYAN).a(taskVenueExtra).reset(), "", "");
			}
			
			else if(venueLines>descriptionLines){
				for(int i=0;i<descriptionLines-1;i++){
					String displayVenue = taskVenueExtra.substring(0,12);
					String displayDescription = taskDescriptionExtra.substring(0,25);
					taskVenueExtra = taskVenueExtra.substring(12);
					taskDescriptionExtra = taskDescriptionExtra.substring(25);
					
					result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED).a(nullSpace).reset(), ansi()
							.fg(MAGENTA).a(displayDescription).reset(),
							ansi().fg(CYAN).a(displayVenue).reset(), "", "");
				}
				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED).a(nullSpace).reset(), ansi()
						.fg(MAGENTA).a(taskDescriptionExtra).reset(),
						ansi().fg(CYAN).a(taskVenueExtra.substring(0,12)).reset(), "", "");
				
				taskVenueExtra = taskVenueExtra.substring(12);
				if(venueLines-descriptionLines-1 == 0) {
					result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED).a(nullSpace).reset(), ansi()
							.fg(MAGENTA).a("").reset(),
							ansi().fg(CYAN).a(taskVenueExtra).reset(), "", "");
				}
				else {
				for (int j=0; j<(venueLines-descriptionLines)-1;j++){
					String displayVenue = taskVenueExtra.substring(0,12);
					taskVenueExtra = taskVenueExtra.substring(12);
					
					result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED).a(nullSpace).reset(), ansi()
							.fg(MAGENTA).a("").reset(),
							ansi().fg(CYAN).a(displayVenue).reset(), "", "");
				}
				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED).a(nullSpace).reset(), ansi()
						.fg(MAGENTA).a("").reset(),
						ansi().fg(CYAN).a(taskVenueExtra).reset(), "", "");
				}
			}
			
			else if(descriptionLines>venueLines) {
				for(int i=0;i<venueLines-1;i++){
					String displayVenue = taskVenueExtra.substring(0,12);
					String displayDescription = taskDescriptionExtra.substring(0,25);
					taskVenueExtra = taskVenueExtra.substring(12);
					taskDescriptionExtra = taskDescriptionExtra.substring(25);
					
					result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED).a(nullSpace).reset(), ansi()
							.fg(MAGENTA).a(displayDescription).reset(),
							ansi().fg(CYAN).a(displayVenue).reset(), "", "");
				}
				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED).a(nullSpace).reset(), ansi()
						.fg(MAGENTA).a(taskDescriptionExtra.substring(0,25)).reset(),
						ansi().fg(CYAN).a(taskVenueExtra).reset(), "", "");
				
				taskDescriptionExtra = taskDescriptionExtra.substring(25);
				if(descriptionLines-venueLines-1 == 0) {
					result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED).a(nullSpace).reset(), ansi()
							.fg(MAGENTA).a(taskDescriptionExtra).reset(),
							ansi().fg(CYAN).a("").reset(), "", "");
				}
				else {
				for (int j=0; j<(descriptionLines-venueLines)-1;j++){
					String displayDescription = taskDescriptionExtra.substring(0,25);
					taskDescriptionExtra = taskDescriptionExtra.substring(25);
					result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED).a(nullSpace).reset(), ansi()
							.fg(MAGENTA).a(displayDescription).reset(),
							ansi().fg(CYAN).a("").reset(), "", "");
				}
				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED).a(nullSpace).reset(), ansi()
						.fg(MAGENTA).a(taskDescriptionExtra).reset(),
						ansi().fg(CYAN).a("").reset(), "", "");
				}
				
			}
			
		}
		
		return result;
	}

	public static String displayTaskForAdd(Task task) {
		result = "";
		result = result + displayTask(task) + MESSAGE_ADDED;
		return result;
	}

	public static String displayTask(Task task) {
		result = "";
		result += getDescription(task);
		result += getVenue(task);
		// if a task has a start date, it will definitely have an end date, it
		// just depends whether they are equal
		if (task.getHasStartDate() && !startDateEqualsEndDate(task)) { // append
																		// "from.. ..to"
			result += addFrom();
			result += addStartDate(task);
			if (task.getHasStartTime()) {
				result += addStartTime(task);
			}
			result += addTo();
			result += addEndDate(task);
			if (task.getHasEndTime()) {
				result += addEndTime(task);
			}
		} else if (task.getHasStartDate()) {
			result += addOn(task);
			result += addStartDate(task);
			
			if (task.getHasStartTime() && task.getHasEndTime()){
				result += addFrom();
				result += addStartTime(task);
				result += addTo();
				result += addEndTime(task);
			}
			else {
				result += addAt(task);
				result += addStartTime(task);
			}
			
		} else if (task.getHasStartTime()) {
			result += addAt(task);
			result += addStartTime(task);
		}

		if (task.getHasRecurrence()) {
			result += addRecurrence(task);
		}

		if (task.getCompleted()) {
			result += addCompleted(task);
		}
		result = result.trim();
		return result;
	}

	private static String getTaskNumber(int number) {
		return number + 1 + "." + STRING_SPACE;
	}

	private static String getDescription(Task task) {
		return (task.getDescription() + STRING_SPACE);
	}

	private static String getVenue(Task task) {
		String venue = task.getVenue();
		if (venue != null) {
			return "at " + task.getVenue() + STRING_SPACE;
		} else {
			return "";
		}
	}

	private static String addFrom() {
		return "from" + STRING_SPACE;
	}

	private static String addTo() {
		return "to" + STRING_SPACE;
	}

	// this function assumes that startDate and endDate will never be null
	private static boolean startDateEqualsEndDate(Task task)
			throws IllegalArgumentException {
		LocalDate startDate = task.getStartDate();
		LocalDate endDate = task.getEndDate();
		DateTime d1 = startDate.toDateTimeAtStartOfDay();
		DateTime d2 = endDate.toDateTimeAtStartOfDay();
		int compareResult = DateTimeComparator.getDateOnlyInstance().compare(
				d1, d2);
		switch (compareResult) {
		case -1: // startDate is earlier than endDate
			return false;
		case 0: // startDate equals endDate
			return true;
		default: // we should never reach this case
			throw new IllegalArgumentException(
					"Unexpected outcome in startDateEqualsEndDate function in DisplayHandler.");
		}
	}

	private static String addStartDate(Task task) {
		return task.getStartDate() + STRING_SPACE;
	}

	private static String addStartTime(Task task) {
		return task.getStartTime().toString()
				.substring(TIME_STRING_START_INDEX, TIME_STRING_END_INDEX)
				+ STRING_SPACE;
	}

	private static String addEndDate(Task task) {
		return task.getEndDate() + STRING_SPACE;
	}

	private static String addEndTime(Task task) {
		return task.getEndTime().toString()
				.substring(TIME_STRING_START_INDEX, TIME_STRING_END_INDEX)
				+ STRING_SPACE;
	}

	private static String addOn(Task task) {
		return "on" + STRING_SPACE;
	}

	private static String addAt(Task task) {
		return "at" + STRING_SPACE;
	}

	private static String addRecurrence(Task task) {
		return "(" + task.getRecurrence() + ")" + STRING_SPACE;
	}

	private static String addCompleted(Task task) {
		return "(" + task.getCompleted() + ")" + STRING_SPACE;
	}

}