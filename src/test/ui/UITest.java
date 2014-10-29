package test.ui;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.CYAN;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.MAGENTA;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.YELLOW;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import main.logic.DisplayHandler;
import main.logic.Logic;
import main.storage.Storage;
import main.storage.Task;
import main.ui.UI;

import org.junit.Test;

public class UITest {

	@Test
	public void test() throws IOException {
		Storage storage = Storage.getInstance();
		ArrayList<Task> tasks = storage.getTasks();

		//Test the UI input for basic CRUD functions
		
		//adding without any description
		String dataOne = "add";
		Logic.uiToLogic(dataOne);
		String expectedOne = "Sorry, we did not capture your description. Please try again.";
		assertEquals(expectedOne,Logic.uiToLogic(dataOne));
		
		tasks.clear();
		//add task with description, venue, date and time
		String dataTwo ="add meeting at clb at 9pm next tuesday";
		Logic.uiToLogic(dataTwo);
		String expectedTwo = "meeting at clb on 2014-11-04 at 21:00 added!";
		assertEquals(expectedTwo,Logic.uiToLogic(dataTwo));
		
		//display task
		String dataThree ="display";
		Logic.uiToLogic(dataThree);
		String result ="";
		result = String.format(DisplayHandler.DISPLAY_NUM_OF_TASKS, tasks.size());

		result += String.format(DisplayHandler.DISPLAY_TABLE_ROW_STRING_FORMAT,
				ansi().fg(RED).a("ID").reset(),
				ansi().fg(MAGENTA).a(" DESCRIPTION").reset(),
				ansi().fg(CYAN).a(" VENUE").reset(),
				ansi().fg(YELLOW).a(" TIME").reset(),
				ansi().fg(GREEN).a(" DATE").reset());
		result += DisplayHandler.displayLineSeparator();
		for(int i=0;i<tasks.size();i++){
			result += DisplayHandler.displayTaskInTable(i, tasks.get(i));
		}
		result += DisplayHandler.displayLineSeparator();
		result += String.format(DisplayHandler.DISPLAY_TABLE_ROW_STRING_FORMAT,
				ansi().fg(RED).a("ID").reset(),
				ansi().fg(MAGENTA).a(" DESCRIPTION").reset(),
				ansi().fg(CYAN).a(" VENUE").reset(),
				ansi().fg(YELLOW).a(" TIME").reset(),
				ansi().fg(GREEN).a(" DATE").reset());		
		assertEquals(result,Logic.uiToLogic(dataThree));
	
	
	//update task venue
	String dataFour = "update 1 venue nlb";
	String expectedFour = "Task 1's venue updated!";
	Logic.uiToLogic(dataFour);
	assertEquals(expectedFour, Logic.uiToLogic(dataFour));
	
	//delete tasks in an empty list
	String dataFive = "delete all";
	String expectedFive = "Sorry, the arguments must only contain the task IDs to be deleted. Please try again.";
	Logic.uiToLogic(dataFive);
	assertEquals(expectedFive, Logic.uiToLogic(dataFive));
	
	}
}
