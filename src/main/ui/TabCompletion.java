package main.ui;

import java.io.IOException;

import jline.ArgumentCompletor;
import jline.ConsoleReader;
import jline.SimpleCompletor;
import main.logic.Logic;
//@author A0100239W
public class TabCompletion {
	private static String line;
	private static final String MESSAGE_PROMPT = "Enter Command: ";
	private static final String DISPLAY_ADD = "\033[1madd:\033[0m\n\n";
	private static final String MESSAGE_ADD = "This is the command to add a new task to Tasker.\nYou can add tasks in a few different formats.\nTo add a new floating task, type \033[1madd\033[0m [description] \033[1mat\033[0m [venue]\n"
			+ "To add a new timed task, type \033[1madd\033[0m [description] \033[1mat\033[0m [venue] \033[1mfrom\033[0m [start date] [start time] \033[1mto\033[0m [end date][end time]\n"
			+ "To add a new deadline task, type \033[1madd\033[0m [description] \033[1mat\033[0m [venue] \033[1mat\033[0m [time]\n"
			+ "For time, the acceptable formats are: \033[1m11am, 11.30pm\033[0m\n"
			+ "For date, the acceptable formats are: \033[1m5 December, 5 December 2014, 5/12, 5/12/2014, Thursday, next Thursday\033[0m\n\n";
	private static final String DISPLAY_DELETE = "\033[1mdelete:\033[0m\n\n";
	private static final String MESSAGE_DELETE = "This is the command to delete task(s) from Tasker.\n"
			+ "To delete a single task, type \033[1mdelete\033[0m [task index]\n"
			+ "To delete multiple tasks, type \033[1mdelete\033[0m [task index] [task index] etc\n\n";
	private static final String DISPLAY_DISPLAY = "\033[1mdisplay:\033[0m\n\n";
	private static final String MESSAGE_DISPLAY = "This is the command to display the lists of tasks you have in Tasker.\n"
			+ "Simply type \033[1mdisplay\033[0m to see the task list.\n\n";
	private static final String DISPLAY_SEARCH = "\033[1msearch:\033[0m\n\n";
	private static final String MESSAGE_SEARCH = "This is the command to search for specific tasks in the list.\n"
			+ "To search for a keyword from the task description/venue, type \033[1msearch\033[0m [keyword]\n"
			+ "To search for tasks due today, type \033[1msearch today\033[0m\n"
			+ "To search for floating tasks, type \033[1msearch floating\033[0m\n"
			+ "To search for past tasks, type \033[1msearch past\033[0m\n"
			+ "To search for tasks due in the future, type \033[1msearch future\033[0m\n\n";
	private static final String DISPLAY_UPDATE = "\033[1mupdate:\033[0m\n\n";
	private static final String MESSAGE_UPDATE = "This is the command to update and edit different aspects of the task.\n"
			+ "To update the description, type \033[1mupdate\033[0m [task index] \033[1mdescription\033[0m [new description]\n"
			+ "To update the venue, type \033[1mupdate\033[0m [task index] \033[1mvenue\033[0m [new venue]\n"
			+ "To update the start time or date, type \033[1mupdate\033[0m [task index] \033[1mstart\033[0m [new time/date]\n"
			+ "To update the end time or date, type \033[1mupdate\033[0m [task index] \033[1mend\033[0m [new time/date]\n"
			+ "For time, the acceptable formats are: \033[1m11am, 11.30pm\033[0m\n"
			+ "For date, the acceptable formats are: \033[1m5 December, 5 December 2014, 5/12, 5/12/2014, Thursday, next Thursday\033[0m\n"
			+ "Do note that for floating tasks, you are unable to update the venue, start time/date and end time/date\n\n";
	private static final String DISPLAY_SYNC = "\033[1msync:\033[0m\n\n";
	private static final String MESSAGE_SYNC = "This is the command to synchronise your task list with your Google Calender.\n"
			+ "To synchronise, type \033[1msync\033[0m\n\n";
	private static final String DISPLAY_EXIT = "\033[1mexit:\033[0m\n\n";
	private static final String MESSAGE_EXIT = "This is the command to exit Tasker. Simply type \033[1mexit\033[0m\n\n";

	public void run() throws IOException {
		ConsoleReader reader = new ConsoleReader();
		String[] availableCommandStrings = new String[] { "add", "search",
				"update", "exit", "display", "help", "sync", "delete" };
		SimpleCompletor simpleCompletor = new SimpleCompletor(availableCommandStrings);

		ArgumentCompletor argumentCompletor = new ArgumentCompletor(
				simpleCompletor);
		reader.addCompletor(argumentCompletor);

		while ((line = readLine(reader, "")) != null) {
			if (line.trim().equalsIgnoreCase("help")) {
				printHelp();
			} else {
				System.out.println(Logic.uiToLogic(line));
			}
		}
	}

	private void printHelp() {
		String result = "\n";
		result += DISPLAY_ADD;
		result += MESSAGE_ADD;
		result += DISPLAY_DELETE;
		result += MESSAGE_DELETE;
		result += DISPLAY_DISPLAY;
		result += MESSAGE_DISPLAY;
		result += DISPLAY_SEARCH;
		result += MESSAGE_SEARCH;
		result += DISPLAY_UPDATE;
		result += MESSAGE_UPDATE;
		result += DISPLAY_SYNC;
		result += MESSAGE_SYNC;
		result += DISPLAY_EXIT;
		result += MESSAGE_EXIT;
		System.out.println(result);
	}

	private String readLine(ConsoleReader reader, String promptMessage)
			throws IOException {

		String line = reader.readLine(promptMessage + MESSAGE_PROMPT);
		return line;
	}

	public static void main(String[] args) throws IOException {
		TabCompletion shell = new TabCompletion();
		shell.run();
	}
}