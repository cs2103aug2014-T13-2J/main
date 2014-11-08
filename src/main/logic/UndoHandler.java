package main.logic;

import main.storage.Storage;

public class UndoHandler extends CommandHandler {
	
	private final static String MESSAGE_UNDO_SUCCESS = "Undo successful.";
	
	private static Storage storage = Storage.getInstance();
	
	public UndoHandler(String details) {
		super(details);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute() {
		try {
			storage.revertTaskHistories();
		} catch(IllegalArgumentException e) {
			return e.getMessage();
		}
		
		return MESSAGE_UNDO_SUCCESS;
	}

}
