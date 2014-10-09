package main.logic;

import java.util.ArrayList;
import java.util.List;

import main.storage.Task;

public class DisplayHandler extends CommandHandler {
public static final String MESSAGE_EMPTY = "There is nothing to display";

	public DisplayHandler(String details) {
		super(details);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String execute() {
		ArrayList<Task> tasks = getCurrentTaskList();
		if(tasks.isEmpty()){
		System.out.println(MESSAGE_EMPTY);
		}
		else{
			for(int i=0; i<tasks.size();i++){
				System.out.println(tasks.get(i));
			}
		}
	return null;
	}

}