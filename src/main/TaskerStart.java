package main;

import java.io.IOException;
import java.net.URISyntaxException;

import main.ui.UI;

public class TaskerStart {
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		UI.initializeEnvironment();
		UI.readAndExecuteCommands();
	}
}
