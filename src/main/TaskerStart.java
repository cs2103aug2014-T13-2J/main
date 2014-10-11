package main;

import java.io.IOException;

import main.ui.UI;

public class TaskerStart {
	
	public static void main(String[] args) throws IOException {
		UI.initializeEnvironment();
		UI.readAndExecuteCommands();
	}
}
