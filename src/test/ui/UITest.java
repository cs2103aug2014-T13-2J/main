package test.ui;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.CYAN;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.MAGENTA;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.YELLOW;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import main.logic.DisplayHandler;
import main.logic.Logic;
import main.storage.Storage;
import main.storage.Task;

import org.junit.Test;

public class UITest {

	@Test
	public void test() throws IOException {
		Storage storage = Storage.getInstance();
		ArrayList<Task> tasks = storage.getTasks();

		// Test the UI input for basic CRUD functions

		// the first 3 cases allow the venue, time and date to be swapped around
		String s1 = "add meeting with Prof at 11pm at CLB next Thursday";
		String e1 = "meeting with Prof at CLB on 2014-11-06 at 23:00 added!";
		assertEquals(e1, Logic.uiToLogic(s1));
		tasks.clear();
		
		String s2 = "add meeting with Prof at CLB at 11pm next Thursday";
		String e2 = "meeting with Prof at CLB on 2014-11-06 at 23:00 added!";
		assertEquals(e2, Logic.uiToLogic(s2));
		tasks.clear();
		
		String s3 = "add meeting with Prof at CLB next Thursday at 11pm";
		String e3 = "meeting with Prof at CLB on 2014-11-06 at 23:00 added!";
		assertEquals(e3, Logic.uiToLogic(s3));
		tasks.clear();
		
		// cases 4 to 7 allow different kinds of time formatting
		String s4 = "add gathering on 3/10/2014";
		String e4 = "gathering on 2014-10-03 added!";
		assertEquals(e4, Logic.uiToLogic(s4));
		tasks.clear();
		
		String s5 = "add gathering on 3/10";
		String e5 = "gathering on 2014-10-03 added!";
		assertEquals(e5, Logic.uiToLogic(s5));
		tasks.clear();
		
		String s6 = "add gathering on 3 October";
		String e6 = "gathering on 2014-10-03 added!";
		assertEquals(e6, Logic.uiToLogic(s6));
		tasks.clear();
		
		String s7 = "add gathering on 3 October 2014";
		String e7 = "gathering on 2014-10-03 added!";
		assertEquals(e7, Logic.uiToLogic(s7));
		tasks.clear();
		
		String s8 = "add meeting from next Thursday 11pm to next Friday 1am Utown";
		String e8 = "gathering added!";
		assertEquals(e8, Logic.uiToLogic(s8));
		tasks.clear();
		
		String s9 = "add meeting from Thursday to Friday at Utown";
		String e9 = "gathering added!";
		assertEquals(e9, Logic.uiToLogic(s9));
		tasks.clear();
		
		String s10 = "add meeting from Thursday 11pm to Friday 1am at Utown";
		String e10 = "gathering added!";
		assertEquals(e10, Logic.uiToLogic(s10));
		tasks.clear();

		String s11 = "add meeting from 3/10 to 18/10";
		String e11 = "gathering added!";
		assertEquals(e11, Logic.uiToLogic(s11));
		
		String s12 = "update 1 description study date";
		String e12 = "gathering added!";
		assertEquals(e12, Logic.uiToLogic(s12));
		
		String s13 = "update 1 venue CLB";
		String e13 = "gathering added!";
		assertEquals(e13, Logic.uiToLogic(s13));
		
		String s14 = "update 1 start 4/10";
		String e14 = "gathering added!";
		assertEquals(e14, Logic.uiToLogic(s14));
		
		String s15 = "update 1 start 11am";
		String e15 = "gathering added!";
		assertEquals(e15, Logic.uiToLogic(s15));
		
		String s16 = "update 1 end 19/10";
		String e16 = "gathering added!";
		assertEquals(e16, Logic.uiToLogic(s16));
		
		String s17 = "update 1 start 2pm";
		String e17 = "gathering added!";
		assertEquals(e17, Logic.uiToLogic(s17));
		
		// display task
		String dataThree = "display";
		Logic.uiToLogic(dataThree);
		String result = "";
		result = String.format(DisplayHandler.DISPLAY_NUM_OF_TASKS,
				tasks.size());

		result += String.format(DisplayHandler.DISPLAY_TABLE_ROW_STRING_FORMAT,
				ansi().fg(RED).a("ID").reset(),
				ansi().fg(MAGENTA).a(" DESCRIPTION").reset(), ansi().fg(CYAN)
						.a(" VENUE").reset(), ansi().fg(YELLOW).a(" TIME")
						.reset(), ansi().fg(GREEN).a(" DATE").reset());
		result += DisplayHandler.displayLineSeparator();
		for (int i = 0; i < tasks.size(); i++) {
			result += DisplayHandler.displayTaskInTable(i, tasks.get(i));
		}
		result += DisplayHandler.displayLineSeparator();
		result += String.format(DisplayHandler.DISPLAY_TABLE_ROW_STRING_FORMAT,
				ansi().fg(RED).a("ID").reset(),
				ansi().fg(MAGENTA).a(" DESCRIPTION").reset(), ansi().fg(CYAN)
						.a(" VENUE").reset(), ansi().fg(YELLOW).a(" TIME")
						.reset(), ansi().fg(GREEN).a(" DATE").reset());
		assertEquals(result, Logic.uiToLogic(dataThree));

		// update task venue
		String dataFour = "update 1 venue nlb";
		String expectedFour = "Task 1's venue updated!";
		Logic.uiToLogic(dataFour);
		assertEquals(expectedFour, Logic.uiToLogic(dataFour));

		// delete tasks in an empty list
		String dataFive = "delete all";
		String expectedFive = "Sorry, the arguments must only contain the task IDs to be deleted. Please try again.";
		Logic.uiToLogic(dataFive);
		assertEquals(expectedFive, Logic.uiToLogic(dataFive));

	}
}
