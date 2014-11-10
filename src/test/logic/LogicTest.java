package test.logic;
import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.CYAN;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.MAGENTA;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.YELLOW;
import static org.junit.Assert.*;

import java.util.ArrayList;

import main.logic.DisplayHandler;
import main.logic.Logic;
import main.storage.Storage;
import main.storage.Task;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class LogicTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	//author A0108429A
	@Test
	public void testLogicClass() {
		Storage storage = Storage.getInstance();
		ArrayList<Task> tasks = storage.getTasks();// display task
		
		String s1 = "add meeting with Prof at 11pm at CLB next Thursday";
		String e1 = "meeting with Prof at CLB on 2014-11-06 at 23:00 added!";
		assertEquals(e1, Logic.uiToLogic(s1));
		Task task1 = tasks.get(0);
		assertEquals("meeting with Prof", task1.getDescription());
		assertEquals("CLB", task1.getVenue().toString());
		assertEquals("2014-11-06", task1.getStartDate().toString());
		assertEquals("2014-11-06", task1.getEndDate().toString());
		assertEquals("23:00:00.000", task1.getStartTime().toString());
		assertEquals("23:00:00.000", task1.getEndTime().toString());
	
		String s2 = "add meeting with Prof at CLB at 11pm next Thursday";
		String e2 = "meeting with Prof at CLB on 2014-11-06 at 23:00 added!";
		assertEquals(e2, Logic.uiToLogic(s2));
		Task task2 = tasks.get(1);
		assertEquals("meeting with Prof", task2.getDescription());
		assertEquals("CLB", task2.getVenue().toString());
		assertEquals("2014-11-06", task2.getStartDate().toString());
		assertEquals("2014-11-06", task2.getEndDate().toString());
		assertEquals("23:00:00.000", task2.getStartTime().toString());
		assertEquals("23:00:00.000", task2.getEndTime().toString());
		
		String s3 = "add meeting with Prof at CLB next Thursday at 11pm";
		String e3 = "meeting with Prof at CLB on 2014-11-06 at 23:00 added!";
		assertEquals(e3, Logic.uiToLogic(s3));
		Task task3 = tasks.get(2);
		assertEquals("meeting with Prof", task3.getDescription());
		assertEquals("CLB", task3.getVenue().toString());
		assertEquals("2014-11-06", task3.getStartDate().toString());
		assertEquals("2014-11-06", task3.getEndDate().toString());
		assertEquals("23:00:00.000", task3.getStartTime().toString());
		assertEquals("23:00:00.000", task3.getEndTime().toString());
			
		// cases 4 to 7 allow different kinds of time formatting
		String s4 = "add gathering on 3/10/2014";
		String e4 = "gathering on 2014-10-03 added!";
		assertEquals(e4, Logic.uiToLogic(s4));
		Task task4 = tasks.get(3);
		assertEquals("gathering", task4.getDescription());
		assertEquals(null, task4.getVenue());
		assertEquals("2014-10-03", task4.getStartDate().toString());
		assertEquals("2014-10-03", task4.getEndDate().toString());
		assertEquals(null, task4.getStartTime());
		assertEquals(null, task4.getEndTime());
		
		String s5 = "add gathering on 3/10";
		String e5 = "gathering on 2014-10-03 added!";
		assertEquals(e5, Logic.uiToLogic(s5));
		Task task5 = tasks.get(4);
		assertEquals("gathering", task5.getDescription());
		assertEquals(null, task5.getVenue());
		assertEquals("2014-10-03", task5.getStartDate().toString());
		assertEquals("2014-10-03", task5.getEndDate().toString());
		assertEquals(null, task5.getStartTime());
		assertEquals(null, task5.getEndTime());
		
		String s6 = "add gathering on 3 October";
		String e6 = "gathering on 2014-10-03 added!";
		assertEquals(e6, Logic.uiToLogic(s6));
		Task task6 = tasks.get(5);
		assertEquals("gathering", task6.getDescription());
		assertEquals(null, task6.getVenue());
		assertEquals("2014-10-03", task6.getStartDate().toString());
		assertEquals("2014-10-03", task6.getEndDate().toString());
		assertEquals(null, task6.getStartTime());
		assertEquals(null, task6.getEndTime());
		
		String s7 = "add gathering on 3 October 2014";
		String e7 = "gathering on 2014-10-03 added!";
		assertEquals(e7, Logic.uiToLogic(s7));
		Task task7 = tasks.get(6);
		assertEquals("gathering", task7.getDescription());
		assertEquals(null, task7.getVenue());
		assertEquals("2014-10-03", task7.getStartDate().toString());
		assertEquals("2014-10-03", task7.getEndDate().toString());
		assertEquals(null, task7.getStartTime());
		assertEquals(null, task7.getEndTime());
		
		String s8 = "add meeting from next Thursday 11pm to next Friday 1am at Utown";
		String e8 = "meeting at Utown from 2014-11-06 23:00 to 2014-11-07 01:00 added!";
		assertEquals(e8, Logic.uiToLogic(s8));
		Task task8 = tasks.get(7);
		assertEquals("meeting", task8.getDescription());
		assertEquals("Utown", task8.getVenue().toString());
		assertEquals("2014-11-06", task8.getStartDate().toString());
		assertEquals("2014-11-07", task8.getEndDate().toString());
		assertEquals("23:00:00.000", task8.getStartTime().toString());
		assertEquals("01:00:00.000", task8.getEndTime().toString());
		
		String s9 = "add meeting from Thursday to Friday at Utown";
		String e9 = "meeting at Utown from 2014-10-30 to 2014-10-31 added!";
		assertEquals(e9, Logic.uiToLogic(s9));
		Task task9 = tasks.get(8);
		assertEquals("meeting", task9.getDescription());
		assertEquals("Utown", task9.getVenue().toString());
		assertEquals("2014-10-30", task9.getStartDate().toString());
		assertEquals("2014-10-31", task9.getEndDate().toString());
		assertEquals(null, task9.getStartTime());
		assertEquals(null, task9.getEndTime());
		
		String s10 = "add meeting from Thursday 11pm to Friday 1am at Utown";
		String e10 = "meeting at Utown from 2014-10-30 23:00 to 2014-10-31 01:00 added!";
		assertEquals(e10, Logic.uiToLogic(s10));
		Task task10 = tasks.get(9);
		assertEquals("meeting", task10.getDescription());
		assertEquals("Utown", task10.getVenue().toString());
		assertEquals("2014-10-30", task10.getStartDate().toString());
		assertEquals("2014-10-31", task10.getEndDate().toString());
		assertEquals("23:00:00.000", task10.getStartTime().toString());
		assertEquals("01:00:00.000", task10.getEndTime().toString());

		String s11 = "add meeting from 3/10 to 18/10";
		String e11 = "meeting from 2014-10-03 to 2014-10-18 added!";
		assertEquals(e11, Logic.uiToLogic(s11));
		Task task11 = tasks.get(10);
		assertEquals("meeting", task11.getDescription());
		assertEquals(null, task11.getVenue());
		assertEquals("2014-10-03", task11.getStartDate().toString());
		assertEquals("2014-10-18", task11.getEndDate().toString());
		assertEquals(null, task11.getStartTime());
		assertEquals(null, task11.getEndTime());
		
		String s12 = "update 11 description study date";
		String e12 = "Task 11's description updated!";
		assertEquals(e12, Logic.uiToLogic(s12));
		assertEquals("study date", task11.getDescription());
		assertEquals(null, task11.getVenue());
		assertEquals("2014-10-03", task11.getStartDate().toString());
		assertEquals("2014-10-18", task11.getEndDate().toString());
		assertEquals(null, task11.getStartTime());
		assertEquals(null, task11.getEndTime());
		
		String s13 = "update 11 venue CLB";
		String e13 = "Task 11's venue updated!";
		assertEquals(e13, Logic.uiToLogic(s13));
		assertEquals("study date", task11.getDescription());
		assertEquals("CLB", task11.getVenue().toString());
		assertEquals("2014-10-03", task11.getStartDate().toString());
		assertEquals("2014-10-18", task11.getEndDate().toString());
		assertEquals(null, task11.getStartTime());
		assertEquals(null, task11.getEndTime());
		
		String s14 = "update 11 start 4/10";
		String e14 = "Task 11's start date updated!";
		assertEquals(e14, Logic.uiToLogic(s14));
		assertEquals("study date", task11.getDescription());
		assertEquals("CLB", task11.getVenue().toString());
		assertEquals("2014-10-04", task11.getStartDate().toString());
		assertEquals("2014-10-18", task11.getEndDate().toString());
		assertEquals(null, task11.getStartTime());
		assertEquals(null, task11.getEndTime());
		
		String s15 = "update 11 start 11am";
		String e15 = "Task 11's start time updated!";
		assertEquals(e15, Logic.uiToLogic(s15));
		assertEquals("study date", task11.getDescription());
		assertEquals("CLB", task11.getVenue().toString());
		assertEquals("2014-10-04", task11.getStartDate().toString());
		assertEquals("2014-10-18", task11.getEndDate().toString());
		assertEquals("11:00:00.000", task11.getStartTime().toString());
		assertEquals("11:00:00.000", task11.getEndTime().toString());
		
		String s16 = "update 11 end 19/10";
		String e16 = "Task 11's end date updated!";
		assertEquals(e16, Logic.uiToLogic(s16));
		assertEquals("study date", task11.getDescription());
		assertEquals("CLB", task11.getVenue().toString());
		assertEquals("2014-10-04", task11.getStartDate().toString());
		assertEquals("2014-10-19", task11.getEndDate().toString());
		assertEquals("11:00:00.000", task11.getStartTime().toString());
		assertEquals("11:00:00.000", task11.getEndTime().toString());
		
		String s17 = "update 11 end 2pm";
		String e17 = "Task 11's end time updated!";
		assertEquals(e17, Logic.uiToLogic(s17));
		assertEquals("study date", task11.getDescription());
		assertEquals("CLB", task11.getVenue().toString());
		assertEquals("2014-10-04", task11.getStartDate().toString());
		assertEquals("2014-10-19", task11.getEndDate().toString());
		assertEquals("11:00:00.000", task11.getStartTime().toString());
		assertEquals("14:00:00.000", task11.getEndTime().toString());
		
		String s18 = "update 7 start 2 October";
		String e18 = "Task 7's start date updated!";
		assertEquals(e18, Logic.uiToLogic(s18));
		assertEquals("gathering", task7.getDescription());
		assertEquals(null, task7.getVenue());
		assertEquals("2014-10-02", task7.getStartDate().toString());
		assertEquals("2014-10-02", task7.getEndDate().toString());
		assertEquals(null, task7.getStartTime());
		assertEquals(null, task7.getEndTime());
		
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
