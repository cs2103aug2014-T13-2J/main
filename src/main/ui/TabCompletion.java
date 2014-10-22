package main.ui;

import java.io.IOException;

import main.logic.Logic;
import jline.ConsoleReader;
import jline.SimpleCompletor;

public class TabCompletion {
	private static String line;
	public static final String MESSAGE_PROMPT = "Enter Command: ";
	public static final String MESSAGE_EXITED = "Exited from Tasker.";
	public static final String MESSAGE_HELP = "help		- Show help";
	public static final String MESSAGE_ADD = "add         	- Add task to Tasker.";
	public static final String MESSAGE_DELETE = "delete      	- Delete task(s) from Tasker.";
	public static final String MESSAGE_DISPLAY = "display		- Display task(s) to user.";
	public static final String MESSAGE_SEARCH = "search 		- Search for specified terms in task list.";
	public static final String MESSAGE_UPDATE = "update          - Update task list with new details.";
	public static final String MESSAGE_EXIT = "exit 		- Exit Tasker.";
	
	public void run() throws IOException {
		ConsoleReader reader = new ConsoleReader();
		reader.addCompletor(new SimpleCompletor(new String[] {
				"These are the commands available:\n", "help  add  delete  display  search  update  exit" }));

		System.out.print(MESSAGE_PROMPT);
		while ((line = reader.readLine()) != null) {
			if ("help".equals(line)) {
				printHelp();
				System.out.print(MESSAGE_PROMPT);
			} else if ("exit".equals(line)) {
				System.out.println(MESSAGE_EXITED);
				return;
			} else {
				System.out.println(Logic.uiToLogic(line));
				System.out.print(MESSAGE_PROMPT);
			}
		}
	}

	private void printHelp() {
		System.out.println(MESSAGE_HELP);
		System.out.println(MESSAGE_ADD);
		System.out.println(MESSAGE_DELETE);
		System.out.println(MESSAGE_DISPLAY);
		System.out.println(MESSAGE_SEARCH);
		System.out.println(MESSAGE_UPDATE);
		System.out.println(MESSAGE_EXIT);
		
	}

	public static void main(String[] args) throws IOException {
		TabCompletion shell = new TabCompletion();
		shell.run();
	}
}