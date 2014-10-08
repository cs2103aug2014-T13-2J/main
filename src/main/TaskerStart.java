package main;

import java.io.IOException;

import main.ui.Tasker;

public class TaskerStart {
	
	public static void main(String[] args) throws IOException {
		Tasker.initializeEnvironment();
		Tasker.readAndExecuteCommands();
		//Tasker.initializeFileForIO(args[0]);
	}
}
