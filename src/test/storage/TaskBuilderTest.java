package test.storage;

import static org.junit.Assert.*;
import main.storage.Task;
import main.storage.TaskBuilder;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
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
		builder.setStartDate(new LocalDate(2014, 10, 20));
		builder.setStartTime(new LocalTime(23, 59));
		builder.setEndDate(new LocalDate(2014, 11, 21));
		builder.setEndTime(new LocalTime(00, 00));
		builder.setReminder(new DateTime(2014, 9, 20, 21, 30, 0, 0));
		builder.setRecurrence("weekly");
		builder.setCompleted(false);
		Task task = builder.buildTask();
		
		assertEquals("meeting", task.getDescription());
		
		assertEquals("CLB", task.getVenue());
		assertEquals("startDate - Year: ", 2014, task.getStartDate().getYear());
		assertEquals("startDate - Month: ", 10, task.getStartDate().getMonthOfYear());
		assertEquals("StartDate - Day: ", 20, task.getStartDate().getDayOfMonth());
		assertEquals("StartTime - Hour: ", 23, task.getStartTime().getHourOfDay());
		assertEquals("StartTime - Minute: ", 59, task.getStartTime().getMinuteOfHour());

		
		assertEquals("endDate - Year: ", 2014, task.getEndDate().getYear());
		assertEquals("endDate - Month: ", 11, task.getEndDate().getMonthOfYear());
		assertEquals("endDate - Day: ", 21, task.getEndDate().getDayOfMonth());
		assertEquals("endTime - Hour: ", 00, task.getEndTime().getHourOfDay());
		assertEquals("endTime - Minute: ", 00, task.getEndTime().getMinuteOfHour());
		
		assertEquals(task.hasReminder(), true);
		
		assertEquals("Reminder - Year: ", 2014, task.getReminder().getYear());
		assertEquals("Reminder - Month: ", 9, task.getReminder().getMonthOfYear());
		assertEquals("Reminder - Day: ", 20, task.getReminder().getDayOfMonth());
		assertEquals("Reminder - Hour: ", 21, task.getReminder().getHourOfDay());
		assertEquals("Reminder - Minute: ", 30, task.getReminder().getMinuteOfHour());
		
		assertEquals(true, task.hasRecurrence());
		assertEquals("weekly", task.getRecurrence());
		assertEquals(false, task.hasCompleted());
		
	}

}
