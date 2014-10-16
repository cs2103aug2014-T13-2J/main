package main.logic;

import main.storage.Storage;

public class UndoHandler extends CommandHandler {

	private static Storage storage = Storage.getInstance();
	
	public UndoHandler(String details) {
		super(details);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute() {
		return null;
	}

}
