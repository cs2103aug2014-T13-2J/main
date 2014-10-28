package main.ui;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import main.logic.Logic;
import jline.ArgumentCompletor;
import jline.ConsoleReader;
import jline.SimpleCompletor;

public class TabCompletion {
	private static String line;
	public static final String MESSAGE_PROMPT = "Enter Command: ";
	public static final String MESSAGE_EXITED = "Exited from Tasker.";
	public static final String MESSAGE_ADD = "add         	- Add task to Tasker.";
	public static final String MESSAGE_DELETE = "delete      	- Delete task(s) from Tasker.";
	public static final String MESSAGE_DISPLAY = "display		- Display task(s) to user.";
	public static final String MESSAGE_SEARCH = "search 		- Search for specified terms in task list.";
	public static final String MESSAGE_UPDATE = "update          - Update task list with new details.";
	public static final String MESSAGE_EXIT = "exit 		- Exit Tasker.";
	@SuppressWarnings("rawtypes")
	List completors = new LinkedList();

	@SuppressWarnings("unchecked")
	public void run() throws IOException {
		ConsoleReader reader = new ConsoleReader();
		reader.addCompletor(new SimpleCompletor(new String[] {
				"\nThese are the commands available:",
				" add  delete  display  search  update help exit" }));

		completors.add(new SimpleCompletor(new String[] { "add",
				"search", "update", "exit", "display", "delete"}));
		reader.addCompletor(new ArgumentCompletor(completors));

		while ((line = readLine(reader, "")) != null) {

			if ("exit".equals(line)) {
				System.out.println(MESSAGE_EXITED);
				return;
				
			} else if ("help".equals(line)) {
				printHelp();

			} else {
				System.out.println(Logic.uiToLogic(line));
			}
		}
	}

	private void printHelp() {
		System.out.println(MESSAGE_ADD);
		System.out.println(MESSAGE_DELETE);
		System.out.println(MESSAGE_DISPLAY);
		System.out.println(MESSAGE_SEARCH);
		System.out.println(MESSAGE_UPDATE);
		System.out.println(MESSAGE_EXIT);

	}

	private String readLine(ConsoleReader reader, String promtMessage)
			throws IOException {

		String line = reader.readLine(promtMessage + MESSAGE_PROMPT);

		return line;
	}

	public static void main(String[] args) throws IOException {
		TabCompletion shell = new TabCompletion();
		shell.run();
	}
}