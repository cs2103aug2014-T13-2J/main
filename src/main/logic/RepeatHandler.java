package main.logic;

import main.storage.Storage;

public class RepeatHandler extends CommandHandler {

	private static Storage storage = Storage.getInstance();
	
	public RepeatHandler(String details) {
		super(details);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute() {
		return null;
	}

}
