package test.storage;

import static org.junit.Assert.*;
import main.storage.Task;
import main.storage.TaskBuilder;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TaskTest {

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
	public void testSetVenue() {
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		builder.setStartDateTime(new DateTime(2014, 10, 20, 9, 0, 0, 0));
		builder.setEndDateTime(new DateTime(2014, 11, 21, 10, 0, 0, 0));
		builder.setReminder(new DateTime(2014, 9, 20, 21, 30, 0, 0));
		builder.setRecurrence("weekly");
		builder.setCompleted(false);
		Task task = builder.buildTask();
		
		task.setVenue("COM1");
		assertEquals("Venue: ", "COM1", task.getVenue());
	}

	@Test
	public void testSetStartDateTime() {
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		builder.setStartDateTime(new DateTime(2014, 10, 20, 9, 0, 0, 0));
		builder.setEndDateTime(new DateTime(2014, 11, 21, 10, 0, 0, 0));
		builder.setReminder(new DateTime(2014, 9, 20, 21, 30, 0, 0));
		builder.setRecurrence("weekly");
		builder.setCompleted(false);
		Task task = builder.buildTask();
		
		task.setStartDateTime(new DateTime(2014, 9, 20, 15, 0, 0, 0));
		assertEquals("Start date and time: ", "2014-09-20T15:00:00.000+08:00", task.getStartDateTime().toString());
	}

	@Test
	public void testSetEndDateTime() {
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		builder.setStartDateTime(new DateTime(2014, 10, 20, 9, 0, 0, 0));
		builder.setEndDateTime(new DateTime(2014, 11, 21, 10, 0, 0, 0));
		builder.setReminder(new DateTime(2014, 9, 20, 21, 30, 0, 0));
		builder.setRecurrence("weekly");
		builder.setCompleted(false);
		Task task = builder.buildTask();
		
		task.setEndDateTime(new DateTime(2014, 12, 25, 23, 59, 0, 0));
		assertEquals("End date and time: ", "2014-12-25T23:59:00.000+08:00", task.getEndDateTime().toString());
	}

	@Test
	public void testSetReminder() {
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		builder.setStartDateTime(new DateTime(2014, 10, 20, 9, 0, 0, 0));
		builder.setEndDateTime(new DateTime(2014, 11, 21, 10, 0, 0, 0));
		builder.setRecurrence("weekly");
		builder.setCompleted(false);
		Task task = builder.buildTask();
		
		task.setReminder(new DateTime(2014, 9, 20, 21, 30, 0, 0));
		assertEquals("has reminder: ", true, task.getHasReminder());
		assertEquals("reminder: ", "2014-09-20T21:30:00.000+08:00", task.getReminder().toString());
		task.setReminder(null);
		assertEquals("has reminder: ", false, task.getHasReminder());
		assertEquals("reminder: ", null, task.getReminder());
	}


	@Test
	public void testSetRecurrence() {
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		Task task = builder.buildTask();
		
		task.setRecurrence("weekly");
		assertEquals("has recurrence: ", true, task.getHasRecurrence());
		assertEquals("recurrence: ", "weekly", task.getRecurrence());
		task.setRecurrence(null);
		assertEquals("has recurrence: ", false, task.getHasRecurrence());
		assertEquals("recurrence: ", null, task.getRecurrence());
	}

	@Test
	public void testSetCompleted() {
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		Task task = builder.buildTask();	
		
		task.setCompleted(true);
	}

	@Test
	public void testConvertToCSVFormat() {
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		Task task = builder.buildTask();
		
		System.out.println(task.convertToCSVFormat());
		
		task.setStartDateTime(new DateTime(2014, 10, 20, 9, 0, 0, 0));
		task.setEndDateTime(new DateTime(2014, 11, 21, 10, 0, 0, 0));
		task.setRecurrence("weekly");
		task.setCompleted(true);
		
		System.out.println(task.convertToCSVFormat());
		
	}

	@Test
	public void testToString() {
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		Task task = builder.buildTask();
		
		System.out.println(task);
	}
}
