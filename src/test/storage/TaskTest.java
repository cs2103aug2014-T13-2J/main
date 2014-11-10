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
	//author A0108429A
	@Test
	public void testSetVenue() {
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		builder.setStartDate(new LocalDate(2014, 10, 20));
		builder.setEndDate(new LocalDate(2014, 11, 21));
		builder.setReminder(new DateTime(2014, 9, 20, 21, 30, 0, 0));
		builder.setRecurrence("weekly");
		builder.setCompleted(false);
		Task task = builder.buildTask();
		
		task.setVenue("COM1");
		assertEquals("Venue: ", "COM1", task.getVenue());
	}

	@Test
	public void testSetStartDate() {
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		builder.setStartDate(new LocalDate(2014, 10, 20));
		builder.setEndDate(new LocalDate(2014, 11, 21));
		builder.setReminder(new DateTime(2014, 9, 20, 21, 30, 0, 0));
		builder.setRecurrence("weekly");
		builder.setCompleted(false);
		Task task = builder.buildTask();
		
		task.setStartDate(new LocalDate(2014, 10, 19));
		assertEquals("Start date: ", "2014-10-19", task.getStartDate().toString());
		assertEquals("has start date: ", true, task.hasStartDate());
	}
	
	//This is the case for the start later than end equivalence partition
	@Test(expected=IllegalArgumentException.class)
	public void testSetStartDateWithStartLaterThanEnd() {
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		builder.setStartDate(new LocalDate(2014, 10, 20));
		builder.setEndDate(new LocalDate(2014, 11, 21));
		builder.setReminder(new DateTime(2014, 9, 20, 21, 30, 0, 0));
		builder.setRecurrence("weekly");
		builder.setCompleted(false);
		Task task = builder.buildTask();
		//this case should throw an exception because we set start to be later than end
		task.setStartDate(new LocalDate(2014, 11, 24));
	}
	//This is the case for the end earlier than start equivalence partition	
	@Test(expected=IllegalArgumentException.class)
	public void testSetStartDateWithEndEarlierThanStart() {
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		builder.setStartDate(new LocalDate(2014, 10, 20));
		builder.setEndDate(new LocalDate(2014, 11, 21));
		builder.setReminder(new DateTime(2014, 9, 20, 21, 30, 0, 0));
		builder.setRecurrence("weekly");
		builder.setCompleted(false);
		Task task = builder.buildTask();
		//this case should throw an exception because we set end to be earlier than start
		task.setEndDate(new LocalDate(2013, 11, 24));
	}
	
	@Test
	public void testSetStartTime() {
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		builder.setStartDate(new LocalDate(2014, 10, 20));
		builder.setStartTime(new LocalTime(12, 30));
		builder.setEndDate(new LocalDate(2014, 11, 21));
		builder.setEndTime(new LocalTime(14, 30));
		builder.setReminder(new DateTime(2014, 9, 20, 21, 30, 0, 0));
		builder.setRecurrence("weekly");
		builder.setCompleted(false);
		Task task = builder.buildTask();
		
		task.setStartTime(new LocalTime(9, 0));
		assertEquals("Start time: ", "09:00:00.000", task.getStartTime().toString());
	}

	@Test
	public void testSetEndDate() {
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		builder.setStartDate(new LocalDate(2014, 10, 20));
		builder.setEndDate(new LocalDate(2014, 11, 21));
		builder.setReminder(new DateTime(2014, 9, 20, 21, 30, 0, 0));
		builder.setRecurrence("weekly");
		builder.setCompleted(false);
		Task task = builder.buildTask();
		
		task.setEndDate(new LocalDate(2014, 12, 25));
		assertEquals("End date: ", "2014-12-25", task.getEndDate().toString());
	}
	
	@Test
	public void testSetEndTime() {
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		builder.setStartDate(new LocalDate(2014, 10, 20));
		builder.setStartTime(new LocalTime(12, 30));
		builder.setEndDate(new LocalDate(2014, 11, 21));
		builder.setEndTime(new LocalTime(14, 30));
		builder.setReminder(new DateTime(2014, 9, 20, 21, 30, 0, 0));
		builder.setRecurrence("weekly");
		builder.setCompleted(false);
		Task task = builder.buildTask();
		
		task.setEndTime(new LocalTime(20, 59));
		assertEquals("End time: ", "20:59:00.000", task.getEndTime().toString());
	}

	@Test
	public void testSetReminder() {
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		builder.setStartDate(new LocalDate(2014, 10, 20));
		builder.setEndDate(new LocalDate(2014, 11, 21));
		builder.setReminder(new DateTime(2014, 9, 20, 21, 30, 0, 0));
		builder.setRecurrence("weekly");
		builder.setCompleted(false);
		Task task = builder.buildTask();
		
		task.setReminder(new DateTime(2014, 9, 20, 21, 30, 0, 0));
		assertEquals("has reminder: ", true, task.hasReminder());
		assertEquals("reminder: ", "2014-09-20T21:30:00.000+08:00", task.getReminder().toString());
		task.setReminder(null);
		assertEquals("has reminder: ", false, task.hasReminder());
		assertEquals("reminder: ", null, task.getReminder());
	}


	@Test
	public void testSetRecurrence() {
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		Task task = builder.buildTask();
		
		task.setRecurrence("weekly");
		assertEquals("has recurrence: ", true, task.hasRecurrence());
		assertEquals("recurrence: ", "weekly", task.getRecurrence());
		task.setRecurrence(null);
		assertEquals("has recurrence: ", false, task.hasRecurrence());
		assertEquals("recurrence: ", null, task.getRecurrence());
	}

	@Test
	public void testSetCompleted() {
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		Task task = builder.buildTask();	
		
		task.setCompleted(true);
		
		assertEquals("completed: ", true, task.hasCompleted());
	}

	@Test
	public void testConvertToCSVFormat() {
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		Task task = builder.buildTask();
		
		//System.out.println(task.convertToCSVFormat());
		
		builder.setStartDate(new LocalDate(2014, 10, 20));
		builder.setEndDate(new LocalDate(2014, 11, 21));
		task.setRecurrence("weekly");
		task.setCompleted(true);
		
		//System.out.println(task.convertToCSVFormat());
		
	}
	
	@Test
	public void testEndIsEarlierThanStart() {
		LocalDate startDate = new LocalDate(2014, 10, 13);
		LocalDate endDate = new LocalDate(2014, 10, 14);
		LocalTime startTime = new LocalTime(10, 0);
		LocalTime endTime = new LocalTime(12, 0);
		//start earlier than end
		assertEquals("End earlier than start: ", false, Task.endIsEarlierThanStart(startDate, startTime, endDate, endTime));
		
		startDate = new LocalDate(2014, 10, 15);
		endDate = new LocalDate(2014, 10, 10);
		startTime = new LocalTime(10, 0);
		endTime = new LocalTime(12, 0);
		//startDate later than endDate
		assertEquals("End earlier than start: ", true, Task.endIsEarlierThanStart(startDate, startTime, endDate, endTime));
		
		startDate = new LocalDate(2014, 10, 10);
		endDate = new LocalDate(2014, 10, 10);
		startTime = new LocalTime(12, 0);
		endTime = new LocalTime(10, 0);
		//For the same date, startTime later than endTime
		assertEquals("End earlier than start: ", true, Task.endIsEarlierThanStart(startDate, startTime, endDate, endTime));
		
		startDate = new LocalDate(2014, 10, 10);
		endDate = new LocalDate(2014, 10, 10);
		startTime = null;
		endTime = null;
		//user did not specify a startTime and endTime and startDate equals endDate
		assertEquals("End earlier than start: ", false, Task.endIsEarlierThanStart(startDate, startTime, endDate, endTime));
		
		startDate = new LocalDate(2014, 10, 10);
		endDate = new LocalDate(2014, 10, 12);
		startTime = null;
		endTime = null;
		//user did not specify a startTime and endTime and startDate is earlier than endDate
		assertEquals("End earlier than start: ", false, Task.endIsEarlierThanStart(startDate, startTime, endDate, endTime));
		
		startDate = new LocalDate(2014, 10, 20);
		endDate = new LocalDate(2014, 10, 10);
		startTime = null;
		endTime = null;
		//user did not specify a startTime and endTime and startDate is later than endDate
		assertEquals("End earlier than start: ", true, Task.endIsEarlierThanStart(startDate, startTime, endDate, endTime)); 
		
		startDate = new LocalDate(2014, 10, 20);
		endDate = new LocalDate(2014, 10, 20);
		startTime = new LocalTime(10, 0);
		endTime = new LocalTime(10, 0);
		//both times are equal
		assertEquals("End earlier than start: ", false, Task.endIsEarlierThanStart(startDate, startTime, endDate, endTime));
	}
}
