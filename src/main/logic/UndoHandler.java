package main.logic;

import main.googlecalendar.GoogleCalendar;
import main.storage.Storage;

public class UndoHandler extends CommandHandler {
	
	private final static String MESSAGE_UNDO_SUCCESS = "Undo successful.";
	
	private static Storage storage = Storage.getInstance();
	private static GoogleCalendar googleCalendar = GoogleCalendar.getInstance();
	
	public UndoHandler(String details) {
		super(details);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute() {
		boolean hasBeenDeleted = googleCalendar.syncDeleteAllTasks();
		try {
			storage.revertTaskHistories();
		} catch(IllegalArgumentException e) {
			return e.getMessage();
		}
		if (hasBeenDeleted) {
			googleCalendar.syncAddPreviousTasks();
		}
		return MESSAGE_UNDO_SUCCESS;
	}

}
