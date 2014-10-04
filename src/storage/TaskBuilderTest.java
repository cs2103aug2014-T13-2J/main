package storage;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TaskBuilderTest {

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

	@Test
	public void testTaskBuilder() {
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		builder.setStartDateTime(new DateTime(2014, 10, 20, 9, 0, 0, 0));
		builder.setEndDateTime(new DateTime(2014, 11, 21, 10, 0, 0, 0));
		builder.setHasReminder(true);
		builder.setReminder(new DateTime(2014, 9, 20, 21, 30, 0, 0));
		builder.setHasRecurrence(true);
		builder.setRecurrence("weekly");
		builder.setCompleted(false);
		Task task = builder.buildTask();
		
		assertEquals("meeting", task.getDescription());
		
		assertEquals("CLB", task.getVenue());
		
		assertEquals("startDateTime - Year: ", 2014, task.getStartDateTime().getYear());
		assertEquals("startDateTime - Month: ", 10, task.getStartDateTime().getMonthOfYear());
		assertEquals("StartDateTime - Day: ", 20, task.getStartDateTime().getDayOfMonth());
		assertEquals("StartDateTime - Hour: ", 9, task.getStartDateTime().getHourOfDay());
		assertEquals("StartDateTime - Minute: ", 0, task.getStartDateTime().getMinuteOfDay());
		
		assertEquals("endDateTime - Year: ", 2014, task.getEndDateTime().getYear());
		assertEquals("endDateTime - Month: ", 10, task.getEndDateTime().getMonthOfYear());
		assertEquals("endDateTime - Day: ", 20, task.getEndDateTime().getDayOfMonth());
		assertEquals("endDateTime - Hour: ", 9, task.getEndDateTime().getHourOfDay());
		assertEquals("endDateTime - Minute: ", 0, task.getEndDateTime().getMinuteOfDay());
		
		assertEquals(task.getHasReminder(), true);
		
		assertEquals("Reminder - Year: ", 2014, task.getEndDateTime().getYear());
		assertEquals("Reminder - Month: ", 10, task.getEndDateTime().getMonthOfYear());
		assertEquals("Reminder - Day: ", 20, task.getEndDateTime().getDayOfMonth());
		assertEquals("Reminder - Hour: ", 9, task.getEndDateTime().getHourOfDay());
		assertEquals("Reminder - Minute: ", 0, task.getEndDateTime().getMinuteOfDay());
		
		assertEquals(true, task.getHasRecurrence());
		assertEquals("weekly", task.getRecurrence());
		assertEquals(false, task.getCompleted());
		
	}

}
