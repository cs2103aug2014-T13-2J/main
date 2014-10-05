package logic;

import java.util.List;

public abstract class CommandHandler {

	public abstract String execute();
	
	protected List<Task> getCurrentTaskList() {
		return null;
	}
}
