package main.ui;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import main.logic.Logic;

public class UI {
	public static String filename;

	public static final String MESSAGE_WELCOME = "Welcome to Tasker!";
	public static final String MESSAGE_PROMPT = "Enter Command:";
	public static final String MESSAGE_EMPTY = "File is empty.";
	
	public static void initializeEnvironment() {
		System.out.println(MESSAGE_WELCOME);
	}
	
	public static void readAndExecuteCommands() throws IOException {
		Scanner scanner = new Scanner(System.in);
		while(true){
			System.out.println(MESSAGE_PROMPT);
			String userCommand = scanner.nextLine();
			System.out.println(Logic.uiToLogic(userCommand));
		}
	}

}
