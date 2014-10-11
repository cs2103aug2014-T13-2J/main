package main.logic;

import org.joda.time.DateTime;

import main.storage.Storage;
import main.storage.Task;
import main.storage.TaskBuilder;

public class AddHandler extends CommandHandler {

	private AddParser parser;

	public AddHandler(String details) {
		super(details);
		parser = new AddParser(details);
	}

	@Override
	public String execute() {
		try {
			//parser will analyze the user input and store each piece of information into its respective
			//attributes
			parser.parse();
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		}
		
		//use TaskBuilder to create the task
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription(parser.getDescription());
		
		Task task = builder.buildTask();
		//store the task in storage
		Storage storage = Storage.getInstance();
		storage.addTask(task);
		return null;
	}

}
